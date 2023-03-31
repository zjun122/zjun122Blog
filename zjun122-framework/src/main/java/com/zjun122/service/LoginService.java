package com.zjun122.service;

import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.entity.User;

public interface LoginService {
    /**
     * 后台用户登录
     * @param user
     * @return
     */
    ResponseResult login(User user);

    /**
     * 后台用户退出
     * @return
     */
    ResponseResult logout();
}
