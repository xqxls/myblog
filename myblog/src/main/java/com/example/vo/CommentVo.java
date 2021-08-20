package com.example.vo;

import com.example.entity.Comment;
import lombok.Data;

/**
 * @Description:评论vo
 * @Author: huzhuo
 * @Date: Created in 2021/4/21 18:07
 */

@Data
public class CommentVo extends Comment {
    private Long authorId;
    private String authorName;
    private String authorAvatar;

}
