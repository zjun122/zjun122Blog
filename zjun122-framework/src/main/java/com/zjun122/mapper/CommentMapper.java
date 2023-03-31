package com.zjun122.mapper;

import com.zjun122.domain.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author zjun122
* @description 针对表【zj_comment(评论表)】的数据库操作Mapper
* @createDate 2023-03-22 20:30:54
* @Entity com.zjun122.domain.entity.Comment
*/
public interface CommentMapper extends BaseMapper<Comment> {
    String selectUserAvatarByUsername(String username);
}




