package com.zjun122.controller;

import cn.hutool.log.Log;
import com.zjun122.annotation.SystemLog;
import com.zjun122.domain.ResponseResult;
import com.zjun122.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList() {
        return articleService.hotArticleList();
    }

    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    @SystemLog(BusinessName = "查询文章")
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable Long id) {
        return articleService.getArticleDetail(id);
    }

    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable Long id) {
        return articleService.updateViewCount(id);
    }
}
