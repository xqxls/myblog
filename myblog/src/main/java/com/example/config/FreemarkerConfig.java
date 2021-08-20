package com.example.config;

import com.example.template.HotsTemplate;
import com.example.template.PostsTemplate;
import com.example.template.TimeAgoMethod;
import com.jagregory.shiro.freemarker.ShiroTags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Description:标签配置
 * @Author: huzhuo
 * @Date: Created in 2021/4/18 15:52
 */

@Configuration
public class FreemarkerConfig {

    @Autowired
    private freemarker.template.Configuration configuration;

    @Autowired
    PostsTemplate postsTemplate;

    @Autowired
    HotsTemplate hotsTemplate;


    @PostConstruct
    public void setUp() {
        configuration.setSharedVariable("timeAgo", new TimeAgoMethod());
        configuration.setSharedVariable("posts", postsTemplate);
        configuration.setSharedVariable("hots", hotsTemplate);
        configuration.setSharedVariable("shiro", new ShiroTags());
    }
}
