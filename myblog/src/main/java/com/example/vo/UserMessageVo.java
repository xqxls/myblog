package com.example.vo;

import com.example.entity.UserMessage;
import lombok.Data;

/**
 * @Description:用户信息vo
 * @Author: huzhuo
 * @Date: Created in 2021/6/3 11:35
 */

@Data
public class UserMessageVo extends UserMessage {

    private String toUserName;
    private String fromUserName;
    private String postTitle;
    private String commentContent;

}