package com.zjun122.job;

import com.zjun122.constants.SystemConstants;
import com.zjun122.domain.entity.Article;
import com.zjun122.service.ArticleService;
import com.zjun122.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    //每隔三分钟更新一次
    @Scheduled(cron = "0 0/3 * * * ?")
    public void updateViewCount() {
        //从redis中获取数据
        Map<String, Integer> map = redisCache.getCacheMap(SystemConstants.ARTICLE_VIEW_COUNT);
        //取出数据后，对map集合进行list集合的转换
        List<Article> articles = map.entrySet().stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());

        //更新到数据库中
        articleService.updateBatchById(articles);
    }
}
