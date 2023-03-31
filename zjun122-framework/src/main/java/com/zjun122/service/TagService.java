package com.zjun122.service;

import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.dto.TagDto;
import com.zjun122.domain.dto.TagListDto;
import com.zjun122.domain.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author zjun122
* @description 针对表【zj_tag(标签)】的数据库操作Service
* @createDate 2023-03-24 11:19:41
*/
public interface TagService extends IService<Tag> {

    /**
     * 分页查询标签
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param tagListDto 标签名和备注
     * @return
     */
    ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    /**
     * 新增tag
     * @param tagDto
     * @return
     */
    ResponseResult addTag(TagDto tagDto);

    /**
     * 根据id获取tag
     * @param id
     * @return
     */
    ResponseResult getTag(Integer id);

    /**
     * 更新tag
     * @param tagDto
     * @return
     */
    ResponseResult updateTag(TagDto tagDto);

    /**
     * 获取所有标签集合
     * @return
     */
    ResponseResult listAllTag();

    ResponseResult delTag(List<Long> id);
}
