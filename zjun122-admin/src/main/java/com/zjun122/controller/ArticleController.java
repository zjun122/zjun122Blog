package com.zjun122.controller;

import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.dto.AddArticleDto;
import com.zjun122.domain.vo.ArticleUpdateVo;
import com.zjun122.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto articleDto) {
        return articleService.addArticle(articleDto);
    }

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, String title, String summary) {
        return articleService.pageList(pageNum, pageSize, title, summary);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticle(@PathVariable Long id) {
        return articleService.getArticle(id);
    }

    @PutMapping()
    public ResponseResult updateArticle(@RequestBody ArticleUpdateVo articleUpdateVo) {
        return articleService.updateArticle(articleUpdateVo);
    }

    @DeleteMapping("/{id}")
    public ResponseResult delArticle(@PathVariable List<Long> id) {
        return articleService.delArticle(id);
    }
}
