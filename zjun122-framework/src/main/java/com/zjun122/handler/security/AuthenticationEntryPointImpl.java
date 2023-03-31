package com.zjun122.handler.security;

import com.alibaba.fastjson.JSON;
import com.zjun122.domain.ResponseResult;
import com.zjun122.enums.AppHttpCodeEnum;
import com.zjun122.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authException) throws IOException, ServletException {
        ResponseResult result = null;
        //当密码错误时，响应该错误信息
        if (authException instanceof BadCredentialsException) {
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(), authException.getMessage());
        } else if (authException instanceof InsufficientAuthenticationException) {
            //当用户未登录时，响应提示登录
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        } else {
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "认证或授权失败");
        }
        //响应数据给前端
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
