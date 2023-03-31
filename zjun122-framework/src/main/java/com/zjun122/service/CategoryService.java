package com.zjun122.service;

import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.dto.CategoryDto;
import com.zjun122.domain.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zjun122
* @description 针对表【zj_category(分类表)】的数据库操作Service
* @createDate 2023-03-21 20:37:25
*/
public interface CategoryService extends IService<Category> {

    /**
     * 前台获取分类集合
     * @return
     */
    ResponseResult getCategoryList();

    /**
     * 后台获取分类集合
     * @return
     */
    ResponseResult listAllCategory();

    ResponseResult pageList(Integer pageNum, Integer pageSize, String name, String status);

}
