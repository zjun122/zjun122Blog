package com.zjun122.filter;

import com.alibaba.fastjson.JSON;
import com.zjun122.constants.SystemConstants;
import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.entity.LoginUser;
import com.zjun122.enums.AppHttpCodeEnum;
import com.zjun122.utils.JwtUtil;
import com.zjun122.utils.RedisCache;
import com.zjun122.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = request.getHeader("token");
        //判断token是否为空
        if (!StringUtils.hasText(token)) {
            //如果token为空，说明过滤器不需要过滤该次请求
            chain.doFilter(request, response);
            return;
        }

        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            //如果获取此token出现了异常，说明该token：token超时或token非法
            //响应告诉前端需要重新登陆
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        //获取用户id，然后从redis中获取用户的信息
        String userId = claims.getSubject();
        LoginUser loginUser = redisCache.getCacheObject(SystemConstants.REDIS_LOGIN + userId);

        //如果获取不到loginUser
        if (Objects.isNull(loginUser)) {
            //可能是登录过期了，提示用户重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        //将用户信息存入到SecurityContextHolder中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        chain.doFilter(request, response);
    }
}
