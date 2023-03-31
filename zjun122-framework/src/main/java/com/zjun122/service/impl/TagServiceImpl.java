package com.zjun122.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.dto.TagDto;
import com.zjun122.domain.dto.TagListDto;
import com.zjun122.domain.entity.LoginUser;
import com.zjun122.domain.entity.Tag;
import com.zjun122.domain.vo.PageVo;
import com.zjun122.domain.vo.TagVo;
import com.zjun122.mapper.ArticleMapper;
import com.zjun122.service.ArticleService;
import com.zjun122.service.TagService;
import com.zjun122.mapper.TagMapper;
import com.zjun122.utils.BeanCopyUtils;
import com.zjun122.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
* @author zjun122
* @description 针对表【zj_tag(标签)】的数据库操作Service实现
* @createDate 2023-03-24 11:19:41
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        LambdaQueryWrapper<Tag> lqw = new LambdaQueryWrapper<>();
        //模糊查询标签名
        lqw.like(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        //模糊查询备注
        lqw.like(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());

        //分页
        Page<Tag> page = new Page<>(pageNum, pageSize);
        page(page, lqw);

        //转换成vo
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(page.getRecords(), TagVo.class);

        PageVo pageVo = new PageVo(tagVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(TagDto tagDto) {
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTag(Integer id) {
        Tag tag = getById(id);
        return ResponseResult.okResult(BeanCopyUtils.copyBean(tag, TagVo.class));
    }

    @Override
    public ResponseResult updateTag(TagDto tagDto) {
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        List<Tag> tagList = list();
        return ResponseResult.okResult(BeanCopyUtils.copyBeanList(tagList, TagVo.class));
    }

    @Override
    public ResponseResult delTag(List<Long> id) {
        removeByIds(id);
/*        for (Long tagId : id) {
            baseMapper.delTagAndArticle(tagId);
        }*/
        return ResponseResult.okResult();
    }
}




