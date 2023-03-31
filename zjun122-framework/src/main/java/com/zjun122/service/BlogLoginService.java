package com.zjun122.service;

import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.entity.User;

public interface BlogLoginService {
    /**
     * 用户登录
     * @param user 用户参数
     * @return
     */
    ResponseResult login(User user);

    /**
     * 用户退出登录
     * @return
     */
    ResponseResult logout();
}
