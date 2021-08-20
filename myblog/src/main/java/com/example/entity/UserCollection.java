package com.example.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description:用户收藏
 * @Author: huzhuo
 * @Date: Created in 2021/4/12 23:02
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserCollection extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long postId;

    private Long postUserId;


}
