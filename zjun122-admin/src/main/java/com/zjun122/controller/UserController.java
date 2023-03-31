package com.zjun122.controller;

import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.dto.RegUserDto;
import com.zjun122.domain.dto.UserChangeStatusDto;
import com.zjun122.domain.dto.UserDto;
import com.zjun122.domain.dto.UserUpdateDto;
import com.zjun122.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        return userService.pageList(pageNum, pageSize, userName, phonenumber, status);
    }

    @PostMapping
    public ResponseResult addUser(@RequestBody RegUserDto userDto) {
        return userService.register(userDto);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody UserChangeStatusDto userChangeStatusDto) {
        return userService.changeStatus(userChangeStatusDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult delUser(@PathVariable List<Long> id) {
        return userService.delUser(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping
    public ResponseResult updateUser(@RequestBody UserUpdateDto userUpdateDto) {
        return userService.updateUser(userUpdateDto);
    }
}
