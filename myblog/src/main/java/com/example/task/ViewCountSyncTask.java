package com.example.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Post;
import com.example.service.PostService;
import com.example.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Description:定时任务
 * @Author: huzhuo
 * @Date: Created in 2021/5/4 20:48
 */

@Component
public class ViewCountSyncTask {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    PostService postService;


    @Scheduled(cron = "0/5 * * * * *") //每分钟同步
    public void task() {

        Set<String> keys = redisTemplate.keys("rank:post:*");

        //获取缓存中含有阅读量的文章id集合
        List<String> ids = new ArrayList<>();
        for (String key : keys) {
            if (redisUtil.hHasKey(key, "post:viewCount")) {
                ids.add(key.substring("rank:post:".length()));
            }
        }

        if (ids.isEmpty()) return;

        // 根据id查找需要更新的文章阅读量
        List<Post> posts = postService.list(new QueryWrapper<Post>().in("id", ids));

        posts.stream().forEach((post) -> {
            Integer viewCount = (Integer) redisUtil.hget("rank:post:" + post.getId(), "post:viewCount");
            post.setViewCount(viewCount);
        });

        if (posts.isEmpty()) return;

        boolean isSucc = postService.updateBatchById(posts);

        //如果同步成功，清空缓存里的阅读量
        if (isSucc) {
            ids.stream().forEach((id) -> {
                redisUtil.hdel("rank:post:" + id, "post:viewCount");
                System.out.println(id + "---------------------->同步成功");
            });
        }
    }

}
