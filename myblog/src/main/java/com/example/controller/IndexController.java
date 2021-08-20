package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description:主页
 * @Author: huzhuo
 * @Date: Created in 2021/4/12 16:46
 */

@Controller
public class IndexController extends BaseController {

    @RequestMapping({"", "/", "index"})
    public String index() {

        int pn = ServletRequestUtils.getIntParameter(req, "pn", 1);
        int size = ServletRequestUtils.getIntParameter(req, "size", 2);
        Page page = new Page(pn, size);
        // 1分页信息 2分类 3用户 4置顶  5精选 6排序
        IPage results = postService.paging(page, null, null, null, null, "created");
        req.setAttribute("pageData", results);
        req.setAttribute("currentCategoryId", 0);

        return "index";
    }

    @RequestMapping("/search")
    public String search(String q) {

        IPage pageData = searchService.search(getPage(), q);

        req.setAttribute("q", q);
        req.setAttribute("pageData", pageData);
        return "search";
    }
}
