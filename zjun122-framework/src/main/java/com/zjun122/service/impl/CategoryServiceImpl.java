package com.zjun122.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjun122.constants.SystemConstants;
import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.entity.Article;
import com.zjun122.domain.entity.Category;
import com.zjun122.domain.vo.CategoryListVo;
import com.zjun122.domain.vo.CategoryVo;
import com.zjun122.domain.vo.PageVo;
import com.zjun122.service.ArticleService;
import com.zjun122.service.CategoryService;
import com.zjun122.mapper.CategoryMapper;
import com.zjun122.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author zjun122
* @description 针对表【zj_category(分类表)】的数据库操作Service实现
* @createDate 2023-03-21 20:37:25
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //查询文章表，状态为已发布的文章
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articles = articleService.list(lqw);

        //获取文章的分类id，并且去重
        Set<Long> categoryIds = articles.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());
        //查询分类表
        List<Category> categories = listByIds(categoryIds).stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());

        //封装vo
        List<CategoryVo> voList = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(voList);
    }

    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        //查询文章状态为正常的文章
        lqw.eq(Category::getStatus, SystemConstants.STATUS_NORMAL);
        List<Category> list = list(lqw);
        List<CategoryVo> voList = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return ResponseResult.okResult(voList);
    }

    @Override
    public ResponseResult pageList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.hasText(name), Category::getName, name);
        lqw.eq(StringUtils.hasText(status), Category::getStatus, status);

        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, lqw);

        List<CategoryListVo> vos = BeanCopyUtils.copyBeanList(page.getRecords(), CategoryListVo.class);
        return ResponseResult.okResult(new PageVo(vos, page.getTotal()));
    }
}




