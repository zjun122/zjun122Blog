package com.zjun122.service;

import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.entity.Link;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zjun122
* @description 针对表【zj_link(友链)】的数据库操作Service
* @createDate 2023-03-22 13:06:47
*/
public interface LinkService extends IService<Link> {

    /**
     * 获取所有审核通过的友链
     * @return
     */
    ResponseResult getAllLink();

    ResponseResult pageList(Integer pageNum, Integer pageSize, String name, String status);
}
