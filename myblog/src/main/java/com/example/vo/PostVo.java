package com.example.vo;

import com.example.entity.Post;
import lombok.Data;

/**
 * @Description:文章vo
 * @Author: huzhuo
 * @Date: Created in 2021/4/15 17:39
 */

@Data
public class PostVo extends Post {
    private Long authorId;
    private String authorName;
    private String authorAvatar;

    private String categoryName;
}
