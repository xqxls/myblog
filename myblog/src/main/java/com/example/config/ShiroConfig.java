package com.example.config;

import cn.hutool.core.map.MapUtil;
import com.example.shiro.AccountRealm;
import com.example.shiro.AuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description:shiro配置
 * @Author: huzhuo
 * @Date: Created in 2021/5/4 21:03
 */

@Slf4j
@Configuration
public class ShiroConfig {

    @Bean
    public SecurityManager securityManager(AccountRealm accountRealm) {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(accountRealm);

        log.info("------------------>securityManager注入成功");

        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);
        // 配置登录的url和登录成功的url
        filterFactoryBean.setLoginUrl("/login");
        filterFactoryBean.setSuccessUrl("/user/center");
        // 配置未授权跳转页面
        filterFactoryBean.setUnauthorizedUrl("/error/403");

        filterFactoryBean.setFilters(MapUtil.of("auth", authFilter()));

        Map<String, String> map = new LinkedHashMap<>();

        map.put("/res/**", "anon");

        map.put("/user/home", "auth");
        map.put("/user/set", "auth");
        map.put("/user/upload", "auth");
        map.put("/user/index", "auth");
        map.put("/user/public", "auth");
        map.put("/user/collection", "auth");
        map.put("/user/mess", "auth");
        map.put("/msg/remove/", "auth");
        map.put("/message/nums/", "auth");

        map.put("/collection/remove/", "auth");
        map.put("/collection/find/", "auth");
        map.put("/collection/add/", "auth");

        map.put("/post/edit", "auth");
        map.put("/post/submit", "auth");
        map.put("/post/delete", "auth");
        map.put("/post/reply/", "auth");

        map.put("/websocket", "anon");
        map.put("/login", "anon");

        filterFactoryBean.setFilterChainDefinitionMap(map);

        return filterFactoryBean;
    }

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }

}
