package com.zjun122.mapper;

import com.zjun122.domain.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author zjun122
* @description 针对表【zj_article(文章表)】的数据库操作Mapper
* @createDate 2023-03-21 16:13:39
* @Entity com.zjun122.domain.entity.Article
*/
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 添加文章对应的标签
     * @param id 文章id
     * @param tags 标签集合
     */
    void addArticleAndTag(@Param("id") Long id, @Param("tags") List<Long> tags);

    /**
     * 查询文章对应的标签
     * @param id
     * @return
     */
    List<Long> getArticleAndTags(Long id);

    /**
     * 根据文章id删除对应的tags
     * @param id
     */
    void delArticleAndTag(Long id);
}




