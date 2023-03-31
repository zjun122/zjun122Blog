package com.zjun122.mapper;

import com.zjun122.domain.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author zjun122
* @description 针对表【zj_tag(标签)】的数据库操作Mapper
* @createDate 2023-03-24 11:19:41
* @Entity com.zjun122.domain.entity.Tag
*/
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 删除标签和文章的关联关系
     * @param tagId
     */
    void delTagAndArticle(@Param("id") Long tagId);
}




