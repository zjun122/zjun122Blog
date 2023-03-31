package com.zjun122.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjun122.constants.SystemConstants;
import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.dto.AddArticleDto;
import com.zjun122.domain.entity.Article;
import com.zjun122.domain.entity.Category;
import com.zjun122.domain.vo.*;
import com.zjun122.service.ArticleService;
import com.zjun122.mapper.ArticleMapper;
import com.zjun122.service.CategoryService;
import com.zjun122.utils.BeanCopyUtils;
import com.zjun122.utils.RedisCache;
import com.zjun122.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author zjun122
* @description 针对表【zj_article(文章表)】的数据库操作Service实现
* @createDate 2023-03-21 16:13:39
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService{

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章，并封装成result返回
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        //必须是正式文章
        lqw.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序（降序排序）
        lqw.orderByDesc(Article::getViewCount);
        //最多查询十条
        Page<Article> page = new Page<>(1, 10);
        page(page, lqw);

        List<Article> list = page.getRecords();

        //使用BeanCopy工具类来获取voList
        List<HotArticleVo> voList = BeanCopyUtils.copyBeanList(list, HotArticleVo.class);

        return ResponseResult.okResult(voList);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        //如果有categoryId，就要查询和该id相同的文章
        lqw.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        //状态是正式发布的
        lqw.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //对isTop的文章进行降序
        lqw.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lqw);

        //使用lambda表达式 获取分类名称
        //这里不需要接收新集合，因为是通过set方法改变分类名称
        page.getRecords().parallelStream()
                //通过获取文章的分类id，调用categoryService层，传入分类id，然后查询分类名称，再设置文章的分类名称
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                //更改文章浏览量的获取操作，从redis中获取
                .map(article -> article.setViewCount(viewCount(article.getId())))
                .collect(Collectors.toList());

        //for循环模式设置分类名称
        /*for (Article article : page.getRecords()) {
            Category category = categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());
        }*/

        //封装查询结果
        List<ArticleVo> voList = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleVo.class);
        //将查询结果和分页属性，封装到pageVo中
        PageVo pageVo = new PageVo(voList, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中查询文章的浏览量
        article.setViewCount(viewCount(article.getId()));
        //转换成Vo
        ArticleDetailVo vo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名称
        Category category = categoryService.getById(vo.getCategoryId());
        if (category != null) {
            vo.setCategoryName(category.getName());
        }

        //封装响应返回
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应的文章id的浏览量
        redisCache.incrementCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, id.toString(), 1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addArticle(AddArticleDto addArticleDto) {
        //开启了事务注解，如果文章标签表保存失败，那原先的文章也不会保存成功
        //1、保存文章
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        save(article);

        ArticleMapper baseMapper = getBaseMapper();
        //2、保存文章对应的标签信息
        if (addArticleDto.getTags() != null && addArticleDto.getTags().size() > 0) {
            baseMapper.addArticleAndTag(article.getId(), addArticleDto.getTags());
        }
        //注意：在添加完文章后，需要往redis中存入一个新文章的浏览量，不然会报空指针异常
        redisCache.setCacheMapValue("article:ViewCount", article.getId().toString(), 0);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult pageList(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.hasText(title), Article::getTitle, title);
        lqw.like(StringUtils.hasText(summary), Article::getSummary, summary);

        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lqw);
        List<ArticleListVo> voList = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);

        return ResponseResult.okResult(new PageVo(voList, page.getTotal()));
    }

    @Override
    @Transactional
    public ResponseResult updateArticle(ArticleUpdateVo updateArticle) {
        Article article = BeanCopyUtils.copyBean(updateArticle, Article.class);
        //这里为了避免定时更新浏览量时出现问题，要手动设置更新时间和更新人
        article.setUpdateBy(SecurityUtils.getUserId());
        article.setUpdateTime(new Date());
        //先保存文章的信息
        updateById(article);

        //更新文章和标签对应关系
        //先删除原来的关联关系
        baseMapper.delArticleAndTag(article.getId());
        //再插入新的关联关系
        if (updateArticle.getTags() != null && updateArticle.getTags().size() > 0) {
            baseMapper.addArticleAndTag(article.getId(), updateArticle.getTags());
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getArticle(Long id) {
        Article article = getById(id);
        //查询文章的tags信息
        List<Long> tags = baseMapper.getArticleAndTags(id);
        ArticleUpdateVo vo = BeanCopyUtils.copyBean(article, ArticleUpdateVo.class);
        vo.setTags(tags);

        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult delArticle(List<Long> id) {
        removeByIds(id);
/*        for (Long articleId : id) {
            baseMapper.delArticleAndTag(articleId);
        }*/
        return ResponseResult.okResult();
    }

    //从redis中查询文章的浏览量
    //封装成一个方法
    private Long viewCount(Long id) {
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, id.toString());
        return viewCount.longValue();
    }
}




