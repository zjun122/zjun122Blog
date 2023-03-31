package com.zjun122.controller;

import com.zjun122.annotation.SystemLog;
import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.dto.RegUserDto;
import com.zjun122.domain.dto.UserDto;
import com.zjun122.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    public ResponseResult userInfo() {
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    @SystemLog(BusinessName = "更新个人信息")
    public ResponseResult updateUserInfo(@RequestBody UserDto userDto) {
        return userService.updateUserInfo(userDto);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody RegUserDto userDto) {
        return userService.register(userDto);
    }

    @PostMapping("/getCheckCode/{username}/{email}")
    public ResponseResult getCheckCode(@PathVariable String username, @PathVariable String email) {
        return userService.getCheckCode(username, email);
    }
}
