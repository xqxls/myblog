package com.example.shiro;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:用户信息
 * @Author: huzhuo
 * @Date: Created in 2021/5/4 20:48
 */

@Data
public class AccountProfile implements Serializable {

    private Long id;

    private String username;
    private String email;
    private String sign;

    private String avatar;
    private String gender;
    private Date created;

    public String getSex() {
        return "0".equals(gender) ? "女" : "男";
    }

}

