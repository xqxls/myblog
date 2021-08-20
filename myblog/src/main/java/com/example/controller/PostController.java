package com.example.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.lang.Result;
import com.example.config.RabbitMqConfig;
import com.example.entity.*;
import com.example.search.mq.PostMqIndexMessage;
import com.example.util.ValidationUtil;
import com.example.vo.CommentVo;
import com.example.vo.PostVo;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @Description:文章相关操作
 * @Author: huzhuo
 * @Date: Created in 2021/4/12 16:55
 */

@Controller
public class PostController extends BaseController {

    @GetMapping("/category/{id:\\d*}")
    public String category(@PathVariable(name = "id") Long id) {
        int pn = ServletRequestUtils.getIntParameter(req, "pn", 1);

        req.setAttribute("currentCategoryId", id);
        req.setAttribute("pn", pn);
        return "/post/category";
    }

    @GetMapping("/post/{id:\\d*}")
    public String detail(@PathVariable(name = "id") Long id) {

        //查询文章详情
        PostVo vo = postService.selectOnePost(new QueryWrapper<Post>().eq("p.id", id));
        Assert.notNull(vo, "文章已被删除");
        req.setAttribute("post", vo);
        req.setAttribute("currentCategoryId", vo.getCategoryId());

        //跟新文章阅读量
        postService.putViewCount(vo);

        //查询文章评论信息
        // 1分页，2文章id，3用户id，排序
        int pn = ServletRequestUtils.getIntParameter(req, "pn", 1);
        int size = ServletRequestUtils.getIntParameter(req, "size", 2);
        IPage<CommentVo> results = commentService.paing(new Page(pn, size), vo.getId(), null, "created");
        req.setAttribute("commentPageData", results);
        return "/post/detail";
    }

    //判断用户是否收藏了文章
    @ResponseBody
    @PostMapping("/collection/find/")
    public Result collectionFind(Long cid) {
        int count = collectionService.count(new QueryWrapper<UserCollection>()
                .eq("user_id", getProfileId())
                .eq("post_id", cid)
        );
        return Result.success(MapUtil.of("collection", count > 0));
    }

    //收藏当前文章
    @ResponseBody
    @PostMapping("/collection/add/")
    public Result collectionAdd(Long cid) {
        Post post = postService.getById(cid);

        Assert.isTrue(post != null, "改帖子已被删除");
        int count = collectionService.count(new QueryWrapper<UserCollection>()
                .eq("user_id", getProfileId())
                .eq("post_id", cid)
        );
        if (count > 0) {
            return Result.fail("你已经收藏");
        }

        UserCollection collection = new UserCollection();
        collection.setUserId(getProfileId());
        collection.setPostId(cid);
        collection.setCreated(new Date());
        collection.setModified(new Date());

        collection.setPostUserId(post.getUserId());

        collectionService.save(collection);
        return Result.success();
    }

    //删除当前收藏文章
    @ResponseBody
    @PostMapping("/collection/remove/")
    public Result collectionRemove(Long cid) {
        Post post = postService.getById(cid);
        Assert.isTrue(post != null, "改帖子已被删除");

        collectionService.remove(new QueryWrapper<UserCollection>()
                .eq("user_id", getProfileId())
                .eq("post_id", cid));

        return Result.success();
    }

    //编辑或发表文章
    @GetMapping("/post/edit")
    public String edit() {
        String id = req.getParameter("id");
        if (!StringUtils.isEmpty(id)) {
            Post post = postService.getById(id);
            Assert.isTrue(post != null, "该帖子已被删除");
            Assert.isTrue(post.getUserId().longValue() == getProfileId().longValue(), "没有权限操作此文章");
            req.setAttribute("post", post);
        }
        req.setAttribute("categories", categoryService.list());
        return "/post/edit";
    }

    //提交文章
    @ResponseBody
    @PostMapping("/post/submit")
    public Result submit(Post post) {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(post);
        if (validResult.hasErrors()) {
            return Result.fail(validResult.getErrors());
        }

        if (post.getId() == null) {
            post.setUserId(getProfileId());

            post.setModified(new Date());
            post.setCreated(new Date());
            post.setCommentCount(0);
            post.setEditMode(null);
            post.setLevel(0);
            post.setRecommend(false);
            post.setViewCount(0);
            post.setVoteDown(0);
            post.setVoteUp(0);
            postService.save(post);

        } else {
            Post tempPost = postService.getById(post.getId());
            Assert.isTrue(tempPost.getUserId().longValue() == getProfileId().longValue(), "无权限编辑此文章！");

            tempPost.setTitle(post.getTitle());
            tempPost.setContent(post.getContent());
            tempPost.setCategoryId(post.getCategoryId());
            postService.updateById(tempPost);
        }

        // 通知消息给mq，告知更新或添加
        amqpTemplate.convertAndSend(RabbitMqConfig.es_exchage, RabbitMqConfig.es_bind_key,
                new PostMqIndexMessage(post.getId(), PostMqIndexMessage.CREATE_OR_UPDATE));

        return Result.success().action("/post/" + post.getId());
    }

    //普通用户删除帖子
    @ResponseBody
    @Transactional
    @PostMapping("/post/delete")
    public Result delete(Long id) {
        Post post = postService.getById(id);

        Assert.notNull(post, "该帖子已被删除");
        Assert.isTrue(post.getUserId().longValue() == getProfileId().longValue(), "无权限删除此文章！");

        postService.removeById(id);

        // 删除相关消息、收藏等
        messageService.removeByMap(MapUtil.of("post_id", id));
        collectionService.removeByMap(MapUtil.of("post_id", id));

        // 通知消息给mq，告知删除文章
        amqpTemplate.convertAndSend(RabbitMqConfig.es_exchage, RabbitMqConfig.es_bind_key,
                new PostMqIndexMessage(post.getId(), PostMqIndexMessage.REMOVE));

        return Result.success().action("/user/index");
    }

    //评论帖子
    @ResponseBody
    @Transactional
    @PostMapping("/post/reply")
    public Result reply(Long jid, String content) {
        Assert.notNull(jid, "找不到对应的文章");
        Assert.hasLength(content, "评论内容不能为空");

        Post post = postService.getById(jid);
        Assert.isTrue(post != null, "该文章已被删除");

        Comment comment = new Comment();
        comment.setPostId(jid);
        comment.setContent(content);
        comment.setUserId(getProfileId());
        comment.setCreated(new Date());
        comment.setModified(new Date());
        comment.setLevel(0);
        comment.setVoteDown(0);
        comment.setVoteUp(0);
        commentService.save(comment);

        //评论数加1
        post.setCommentCount(post.getCommentCount() + 1);
        postService.updateById(post);

        //本周热议数加1
        postService.incrCommentCountAndUnionForWeekRank(post.getId(), true);

        //通知作者，有人评论了文章
        if (comment.getUserId() != post.getUserId()) {
            UserMessage message = new UserMessage();
            message.setPostId(jid);
            message.setCommentId(comment.getId());
            message.setFromUserId(getProfileId());
            message.setToUserId(post.getUserId());
            message.setType(1);
            message.setContent(content);
            message.setCreated(new Date());
            message.setStatus(0);
            messageService.save(message);

            // 即时通知作者（websocket）
            wsService.sendMessCountToUser(message.getToUserId());

        }

        //通知被@的人
        if (content.startsWith("@")) {
            String username = content.substring(1, content.indexOf(" "));
            User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
            if (user != null) {
                UserMessage message = new UserMessage();
                message.setPostId(jid);
                message.setCommentId(comment.getId());
                message.setFromUserId(getProfileId());
                message.setToUserId(user.getId());
                message.setType(2);
                message.setContent(content);
                message.setCreated(new Date());
                message.setStatus(0);
                messageService.save(message);

                // 即时通知被@的用户
                wsService.sendMessCountToUser(user.getId());
            }
        }


        return Result.success().action("/post/" + post.getId());
    }

    //删除评论
    @ResponseBody
    @Transactional
    @PostMapping("/post/jieda-delete/")
    public Result reply(Long id) {

        Assert.notNull(id, "评论id不能为空！");

        Comment comment = commentService.getById(id);

        Assert.notNull(comment, "找不到对应评论！");

        if (comment.getUserId().longValue() != getProfileId().longValue()) {
            return Result.fail("不是你发表的评论！");
        }
        commentService.removeById(id);

        // 评论数量减一
        Post post = postService.getById(comment.getPostId());
        post.setCommentCount(post.getCommentCount() - 1);
        postService.saveOrUpdate(post);

        //评论数量减一
        postService.incrCommentCountAndUnionForWeekRank(comment.getPostId(), false);

        return Result.success(null);
    }

}
