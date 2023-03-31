package com.zjun122.controller;

import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.entity.User;
import com.zjun122.enums.AppHttpCodeEnum;
import com.zjun122.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {
        //提示必须要填写用户名
        if (!StringUtils.hasText(user.getUserName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout() {
        return blogLoginService.logout();
    }
}
