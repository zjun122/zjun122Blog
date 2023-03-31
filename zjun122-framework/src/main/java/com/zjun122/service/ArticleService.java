package com.zjun122.service;

import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.dto.AddArticleDto;
import com.zjun122.domain.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjun122.domain.vo.ArticleUpdateVo;

import java.util.List;

/**
* @author zjun122
* @description 针对表【zj_article(文章表)】的数据库操作Service
* @createDate 2023-03-21 16:13:39
*/
public interface ArticleService extends IService<Article> {

    /**
     * 获取热门的十篇文章（浏览量前十）
     * @return
     */
    ResponseResult hotArticleList();

    /**
     * 获取文章列表
     * @param pageNum 当前页数
     * @param pageSize 查询多少条
     * @param categoryId 分类id
     * @return
     */
    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    /**
     * 根据id查询文章详情页
     * @param id 文章id
     * @return
     */
    ResponseResult getArticleDetail(Long id);

    /**
     * 更新文章浏览量
     * @param id
     * @return
     */
    ResponseResult updateViewCount(Long id);

    /**
     * 添加博文
     * @param addArticleDto
     * @return
     */
    ResponseResult addArticle(AddArticleDto addArticleDto);

    ResponseResult pageList(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult updateArticle(ArticleUpdateVo updateArticle);

    ResponseResult getArticle(Long id);

    ResponseResult delArticle(List<Long> id);
}
