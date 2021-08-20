package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vo.CommentVo;

/**
 * @Description:评论接口
 * @Author: huzhuo
 * @Date: Created in 2021/4/12 23:02
 */

public interface CommentService extends IService<Comment> {

    IPage<CommentVo> paing(Page page, Long postId, Long userId, String order);
}
