package com.example.search.mq;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:创建或移除索引
 * @Author: huzhuo
 * @Date: Created in 2021/5/8 22:49
 */

@Data
@AllArgsConstructor
public class PostMqIndexMessage implements Serializable {

    // 两种type
    public final static String CREATE_OR_UPDATE = "create_update";
    public final static String REMOVE = "remove";

    private Long postId;
    private String type;

}
