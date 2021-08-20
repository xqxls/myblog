package com.example.im.vo;

import lombok.Data;

/**
 * @Description:目标用户
 * @Author: huzhuo
 * @Date: Created in 2021/5/8 22:49
 */

@Data
public class ImTo {

    private Long id;
    private String username;
    private String type;
    private String avatar;
    private Integer members;

}
