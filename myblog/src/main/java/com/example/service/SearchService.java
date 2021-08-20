package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.search.mq.PostMqIndexMessage;
import com.example.vo.PostVo;

import java.util.List;

/**
 * @Description:搜索接口
 * @Author: huzhuo
 * @Date: Created in 2021/4/12 23:02
 */

public interface SearchService {

    IPage search(Page page, String keyword);

    int initEsData(List<PostVo> records);

    void createOrUpdateIndex(PostMqIndexMessage message);

    void removeIndex(PostMqIndexMessage message);

}
