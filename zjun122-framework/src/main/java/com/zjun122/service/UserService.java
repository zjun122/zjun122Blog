package com.zjun122.service;

import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.dto.RegUserDto;
import com.zjun122.domain.dto.UserChangeStatusDto;
import com.zjun122.domain.dto.UserDto;
import com.zjun122.domain.dto.UserUpdateDto;
import com.zjun122.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author zjun122
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2023-03-22 22:09:28
*/
public interface UserService extends IService<User> {

    /**
     * 查询用户信息
     * @return
     */
    ResponseResult userInfo();

    /**
     * 修改用户信息
     * @param userDto 用户信息
     * @return
     */
    ResponseResult updateUserInfo(UserDto userDto);

    /**
     * 注册用户
     * @param userDto 用户信息
     * @return
     */
    ResponseResult register(RegUserDto userDto);

    ResponseResult pageList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult changeStatus(UserChangeStatusDto userChangeStatusDto);

    ResponseResult delUser(List<Long> id);

    ResponseResult getUser(Long id);

    ResponseResult updateUser(UserUpdateDto userUpdateDto);

    /**
     * 获取邮箱验证码
     * @param username
     * @param email
     * @return
     */
    ResponseResult getCheckCode(String username, String email);
}
