package com.zjun122.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjun122.constants.SystemConstants;
import com.zjun122.domain.entity.Article;
import com.zjun122.mapper.ArticleMapper;
import com.zjun122.utils.RedisCache;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //查询文章浏览量信息 id 和 viewCount
        List<Article> articleList = articleMapper.selectList(lqw);
        Map<String, Integer> viewCountMap = articleList.parallelStream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));
        //将浏览信息存储到redis中
        redisCache.setCacheMap("article:ViewCount", viewCountMap);
    }
}
