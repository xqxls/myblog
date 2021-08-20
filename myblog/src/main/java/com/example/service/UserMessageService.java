package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.UserMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vo.UserMessageVo;

import java.util.List;

/**
 * @Description:用户消息接口
 * @Author: huzhuo
 * @Date: Created in 2021/4/12 23:02
 */

public interface UserMessageService extends IService<UserMessage> {

    IPage paging(Page page, QueryWrapper<UserMessage> wrapper);

    void updateToReaded(List<Long> ids);
}
