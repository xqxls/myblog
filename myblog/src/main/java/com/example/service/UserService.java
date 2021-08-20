package com.example.service;

import com.example.common.lang.Result;
import com.example.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shiro.AccountProfile;

/**
 * @Description:用户接口
 * @Author: huzhuo
 * @Date: Created in 2021/4/12 23:02
 */

public interface UserService extends IService<User> {

    Result register(User user);

    AccountProfile login(String email, String password);
}
