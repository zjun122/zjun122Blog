package com.zjun122.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjun122.constants.SystemConstants;
import com.zjun122.domain.entity.LoginUser;
import com.zjun122.domain.entity.User;
import com.zjun122.mapper.UserMapper;
import com.zjun122.service.MenuService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUserName, username);
        User user = userMapper.selectOne(lqw);
        //判断是否查询到用户，如果查询不到就抛出异常
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }
        //查询对应的用户权限信息
        if (user.getType().equals(SystemConstants.ADMIN)) {
            List<String> perms = menuService.selectPermsByUserId(user.getId());
            return new LoginUser(user, perms);
        }

        //封装成LoginUser返回
        return new LoginUser(user, null);
    }
}
