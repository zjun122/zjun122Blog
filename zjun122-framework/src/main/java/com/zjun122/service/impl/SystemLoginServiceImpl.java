package com.zjun122.service.impl;

import com.zjun122.constants.SystemConstants;
import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.entity.LoginUser;
import com.zjun122.domain.entity.User;
import com.zjun122.service.LoginService;
import com.zjun122.utils.JwtUtil;
import com.zjun122.utils.RedisCache;
import com.zjun122.utils.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SystemLoginServiceImpl implements LoginService {

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
        redisCache.setCacheObject(SystemConstants.REDIS_LOGIN + userId, loginUser);

        //将token，封装到map中，并返回响应
        Map<String, Object> map = new HashMap<>();
        map.put("token", jwt);

        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        //获取当前登录用户的id
        Long userId = SecurityUtils.getUserId();
        //从redis中删除用户信息
        redisCache.deleteObject(SystemConstants.REDIS_LOGIN + userId);
        return ResponseResult.okResult();
    }
}
