package com.zjun122.controller;

import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.entity.LoginUser;
import com.zjun122.domain.entity.User;
import com.zjun122.domain.vo.AdminUserInfoVo;
import com.zjun122.domain.vo.UserInfoVo;
import com.zjun122.enums.AppHttpCodeEnum;
import com.zjun122.service.LoginService;
import com.zjun122.service.MenuService;
import com.zjun122.service.RoleService;
import com.zjun122.utils.BeanCopyUtils;
import com.zjun122.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) {
        //提示必须要填写用户名
        if (!StringUtils.hasText(user.getUserName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout() {
        return loginService.logout();
    }

    @GetMapping("getInfo")
    public ResponseResult getInfo() {
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //获取用户信息
        User user = loginUser.getUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(user.getId());
        //根据用户id查询角色信息
        List<String> roles = roleService.selectRoleKeyByUserId(user.getId());

        //封装数据返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roles, BeanCopyUtils.copyBean(user, UserInfoVo.class));
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("getRouters")
    public ResponseResult getRouters() {
        return menuService.selectRouterMenuTreeByUserId();
    }
}
