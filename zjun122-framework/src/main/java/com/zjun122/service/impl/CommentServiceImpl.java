package com.zjun122.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjun122.constants.SystemConstants;
import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.entity.Article;
import com.zjun122.domain.entity.Comment;
import com.zjun122.domain.vo.CommentVo;
import com.zjun122.domain.vo.PageVo;
import com.zjun122.enums.AppHttpCodeEnum;
import com.zjun122.handler.exception.SystemException;
import com.zjun122.service.ArticleService;
import com.zjun122.service.CommentService;
import com.zjun122.mapper.CommentMapper;
import com.zjun122.service.UserService;
import com.zjun122.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author zjun122
* @description 针对表【zj_comment(评论表)】的数据库操作Service实现
* @createDate 2023-03-22 20:30:54
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论

        LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();
        //对articleId进行判断
        lqw.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType), Comment::getArticleId, articleId);

        //根评论 rootId为-1
        lqw.eq(Comment::getRootId, SystemConstants.COMMENT_ROOT);

        //评论类型
        lqw.eq(Comment::getType, commentType);

        //分页查询
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, lqw);

        //通过下面的方法进行转换和封装
        List<CommentVo> commentVoList = getCommentVoList(page.getRecords());

        //查询所有根评论对应的子评论集合，并且赋值给相对应的属性
        commentVoList.parallelStream()
                .map(commentVo -> commentVo.setChildren(getChildren(commentVo.getId())))
                .map(commentVo -> commentVo.setAvatar(baseMapper.selectUserAvatarByUsername(commentVo.getUsername())))
                .collect(Collectors.toList());


        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        /*LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Article::getIsComment, "1");
        lqw.eq(Article::getId, comment.getArticleId());*/
        Article article = articleService.getById(comment.getArticleId());
        if (!SystemConstants.ARTICLE_IS_COMMENT.equals(article.getIsComment())) {
            throw new SystemException(AppHttpCodeEnum.ARTICLE_NO_COMMENT);
        }

        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 根据根评论的id查询所对应的子评论的集合
     * @param id 根评论的id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();
        //通过根评论id判断
        lqw.eq(Comment::getRootId, id);
        //通过创建时间来排序（降序）
        lqw.orderByAsc(Comment::getCreateTime);

        List<Comment> comments = list(lqw);
        //将当前集合转换成Vo集合并返回
        List<CommentVo> commentVoList = getCommentVoList(comments);
        commentVoList.parallelStream()
                .map(commentVo -> commentVo.setAvatar(baseMapper.selectUserAvatarByUsername(commentVo.getUsername())))
                .collect(Collectors.toList());

        return commentVoList;
    }

    private List<CommentVo> getCommentVoList(List<Comment> list) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //通过stream流遍历集合
        commentVos.parallelStream()
                //通过createBy查询用户名
                .map(commentVo -> commentVo.setUsername(userService.getById(commentVo.getCreateBy()).getNickName()))
                //先进行判断：要回复的人的id是否存在，如果为-1就是不存在
                //然后通过toCommentUserId查询要回复的人的id
                .map(commentVo -> commentVo.getToCommentUserId() != SystemConstants.COMMENT_ROOT ?
                        commentVo.setToCommentUserName(userService.getById(commentVo.getToCommentUserId()).getNickName()) : commentVo)
                .collect(Collectors.toList());

        return commentVos;
    }
}




