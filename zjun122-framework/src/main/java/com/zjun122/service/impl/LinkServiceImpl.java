package com.zjun122.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjun122.constants.SystemConstants;
import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.entity.Category;
import com.zjun122.domain.entity.Link;
import com.zjun122.domain.vo.CategoryListVo;
import com.zjun122.domain.vo.LinkListVo;
import com.zjun122.domain.vo.LinkVo;
import com.zjun122.domain.vo.PageVo;
import com.zjun122.service.LinkService;
import com.zjun122.mapper.LinkMapper;
import com.zjun122.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author zjun122
* @description 针对表【zj_link(友链)】的数据库操作Service实现
* @createDate 2023-03-22 13:06:47
*/
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService{

    @Override
    public ResponseResult getAllLink() {
        //查询审核通过的友链
        LambdaQueryWrapper<Link> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(lqw);
        //转换成Vo
        List<LinkVo> voList = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        //封装返回
        return ResponseResult.okResult(voList);
    }

    @Override
    public ResponseResult pageList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.hasText(name), Link::getName, name);
        lqw.eq(StringUtils.hasText(status), Link::getStatus, status);

        Page<Link> page = new Page<>(pageNum, pageSize);
        page(page, lqw);

        List<LinkListVo> vos = BeanCopyUtils.copyBeanList(page.getRecords(), LinkListVo.class);
        return ResponseResult.okResult(new PageVo(vos, page.getTotal()));

    }
}




