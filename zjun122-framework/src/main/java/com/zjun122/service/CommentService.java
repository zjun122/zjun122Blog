package com.zjun122.service;

import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zjun122
* @description 针对表【zj_comment(评论表)】的数据库操作Service
* @createDate 2023-03-22 20:30:54
*/
public interface CommentService extends IService<Comment> {

    /**
     * 根据文章id查询评论
     *
     * @param commentType 评论类型
     * @param articleId 文章id
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return
     */
    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    /**
     * 新增评论
     * @param comment 评论内容
     * @return
     */
    ResponseResult addComment(Comment comment);
}
