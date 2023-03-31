package com.zjun122.controller;

import com.zjun122.constants.SystemConstants;
import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.dto.AddCommentDto;
import com.zjun122.domain.entity.Comment;
import com.zjun122.service.CommentService;
import com.zjun122.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论", description = "评论相关接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    @ApiOperation(value = "文章评论", notes = "根据文章id获取文章下的评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "文章id"),
            @ApiImplicitParam(name = "pageNum", value = "页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数")
    })
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT, articleId, pageNum, pageSize);
    }

    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto) {
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    public ResponseResult linkComment(Integer pageNum, Integer pageSize) {
        return commentService.commentList(SystemConstants.LINK_COMMENT, null, pageNum, pageSize);
    }
}
