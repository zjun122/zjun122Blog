package com.zjun122.service.impl;

import com.zjun122.constants.SystemConstants;
import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.entity.LoginUser;
import com.zjun122.domain.entity.User;
import com.zjun122.domain.vo.UserInfoVo;
import com.zjun122.enums.AppHttpCodeEnum;
import com.zjun122.service.BlogLoginService;
import com.zjun122.utils.BeanCopyUtils;
import com.zjun122.utils.JwtUtil;
import com.zjun122.utils.RedisCache;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }

        //获取userId，生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

        if (!SystemConstants.STATUS_NORMAL.equals(loginUser.getUser().getStatus())) {
            throw new RuntimeException("用户已停用");
//            return ResponseResult.errorResult(AppHttpCodeEnum.USER_DEACTIVATE);
        }

        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //将用户信息存入到redis中
        redisCache.setCacheObject(SystemConstants.REDIS_BLOG_LOGIN + userId, loginUser);
        //将user转换成userInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);

        //将userInfo和token，封装到map中，并返回响应
        Map<String, Object> map = new HashMap<>();
        map.put("token", jwt);
        map.put("userInfo", userInfoVo);

        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        //从SecurityContextHolder中获取出loginUser
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取当前用户的id
        Long id = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject(SystemConstants.REDIS_BLOG_LOGIN + id);
        //响应结果
        return ResponseResult.okResult();
    }
}
