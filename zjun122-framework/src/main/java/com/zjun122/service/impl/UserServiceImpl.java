package com.zjun122.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjun122.constants.SystemConstants;
import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.dto.RegUserDto;
import com.zjun122.domain.dto.UserChangeStatusDto;
import com.zjun122.domain.dto.UserDto;
import com.zjun122.domain.dto.UserUpdateDto;
import com.zjun122.domain.entity.LoginUser;
import com.zjun122.domain.entity.Role;
import com.zjun122.domain.entity.User;
import com.zjun122.domain.vo.*;
import com.zjun122.enums.AppHttpCodeEnum;
import com.zjun122.mapper.UserMapper;
import com.zjun122.service.RoleService;
import com.zjun122.service.UserService;
import com.zjun122.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
* @author zjun122
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2023-03-22 22:09:28
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private RedisCache redisCache;

    @Resource
    private RoleService roleService;

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(UserDto userDto) {
        //从Security中获取loginUser
        LoginUser loginUser = SecurityUtils.getLoginUser();

        //判断传递过来的userid和token中的userid是否一致
        if (!Objects.equals(loginUser.getUser().getId(), userDto.getId())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.USER_ERROR);
        }

        //对传过来的数据字段判断是否为空
        if (!StringUtils.hasText(userDto.getNickName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(userDto.getAvatar())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.AVATAR_NOT_NULL);
        }
        if (!StringUtils.hasText(userDto.getSex())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SEX_NOT_NULL);
        }

/*        //判断昵称是否重复
        if (nickNameExist(userDto.getNickName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NICKNAME_EXIST);
        }*/

        //因为这里的数据库实体类是user，不是userDto，所以要进行转换操作
        //从数据库中查询当前user的信息，并设置进去
        User user = getById(loginUser.getUser().getId());
        user.setAvatar(userDto.getAvatar());
        user.setEmail(userDto.getEmail());
        user.setNickName(userDto.getNickName());
        user.setSex(userDto.getSex());
        //修改user信息
        updateById(user);
        //并将user信息设置到loginUser中
        loginUser.setUser(user);
        //修改redis中的loginUser信息
        redisCache.setCacheObject(SystemConstants.REDIS_BLOG_LOGIN + loginUser.getUser().getId(), loginUser);

        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult register(RegUserDto userDto) {

        //判断是否为前台用户注册
        //如果是前台用户，则需要邮箱验证码来完成验证
        if (SystemConstants.IS_REG_USER.equals(userDto.getIsReg())) {
            String isEmail = redisCache.getCacheObject("用户：" + userDto.getUserName() + SystemConstants.USER_EMAIL_KEY);
            String isCode = redisCache.getCacheObject("用户：" + userDto.getUserName() + SystemConstants.EMAIL_CODE_KEY);

            if (!StringUtils.hasText(userDto.getCheckCode())) {
                throw new RuntimeException("验证码有误，请重试");
            }

            //判断邮箱是否存在或过期
            if (!StringUtils.hasText(isEmail)) {
                throw new RuntimeException("邮箱已过期，请重新获取验证码");
            } else if (!userDto.getEmail().equals(isEmail)) {
                throw new RuntimeException("邮箱有误，请重新输入");
            } else if (!StringUtils.hasText(isCode)) {
                throw new RuntimeException("验证码过期，请重新发送");
            } else if (!userDto.getCheckCode().equals(isCode)) {
                throw new RuntimeException("验证码有误，请重试");
            }
        }


        //对传过来的数据字段判断是否为空
        if (!StringUtils.hasText(userDto.getUserName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(userDto.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(userDto.getEmail())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(userDto.getNickName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }

        //对用户名和昵称判断是否已经存在
        if (userNameExist(userDto.getUserName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(userDto.getNickName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NICKNAME_EXIST);
        }



        String encode = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encode);
        userDto.setId(SnowflakeUtils.snowflakeId());

        User user = BeanCopyUtils.copyBean(userDto, User.class);
        save(user);

        //如果要注册的用户中有角色id属性，则添加对应的角色关系
        if (userDto.getRoleIds() != null && userDto.getRoleIds().size() > 0) {
            baseMapper.addUserAndRole(user.getId(), userDto.getRoleIds());
        }

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult pageList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.hasText(userName), User::getUserName, userName);
        lqw.like(StringUtils.hasText(phonenumber), User::getPhonenumber, phonenumber);
        lqw.eq(StringUtils.hasText(status), User::getStatus, status);

        Page<User> page = new Page<>(pageNum, pageSize);
        page(page, lqw);

        List<UserListVo> vos = BeanCopyUtils.copyBeanList(page.getRecords(), UserListVo.class);
        return ResponseResult.okResult(new PageVo(vos, page.getTotal()));
    }

    @Override
    public ResponseResult changeStatus(UserChangeStatusDto userChangeStatusDto) {
        if (userChangeStatusDto.getStatus().equals("0") || userChangeStatusDto.getStatus().equals("1")) {
            baseMapper.updateUserStatus(userChangeStatusDto.getUserId(), userChangeStatusDto.getStatus());
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delUser(List<Long> ids) {
        boolean flag = ids.stream().anyMatch(id -> id.equals(SecurityUtils.getUserId()));
        //如果删除了当前正在操作的用户，则返回错误信息
        if (flag) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_USER_ERROR);
        }
        removeByIds(ids);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUser(Long id) {
        //查询用户信息，封装到UserVo属性中
        UserUpdateVo userUpdateVo = new UserUpdateVo();
        User user = getById(id);
        userUpdateVo.setUser(BeanCopyUtils.copyBean(user, UserVo.class));

        //查询所有状态可用的角色信息，封装到roles中
        LambdaQueryWrapper<Role> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> roleList = roleService.list(lqw);
        userUpdateVo.setRoles(roleList);

        //查询当前用户关联的角色id信息，封装到roleIds中
        List<Long> roleIds = baseMapper.selectUserAndRole(id);
        userUpdateVo.setRoleIds(roleIds);

        return ResponseResult.okResult(userUpdateVo);
    }

    @Override
    @Transactional
    public ResponseResult updateUser(UserUpdateDto userUpdateDto) {
        User user = BeanCopyUtils.copyBean(userUpdateDto, User.class);
        updateById(user);

        baseMapper.delUserAndRole(userUpdateDto.getId());
        if (userUpdateDto.getRoleIds() != null && userUpdateDto.getRoleIds().size() > 0) {
            baseMapper.addUserAndRole(userUpdateDto.getId(), userUpdateDto.getRoleIds());
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCheckCode(String username, String email) {
        String isCode = MailUtils.sendMail(email);

        redisCache.setCacheObject("用户：" + username + SystemConstants.USER_EMAIL_KEY, email, 60, TimeUnit.MINUTES);
        redisCache.setCacheObject("用户：" + username + SystemConstants.EMAIL_CODE_KEY, isCode, 5, TimeUnit.MINUTES);
        return ResponseResult.okResult("验证码发送成功");
    }


    private boolean userNameExist(String username) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUserName, username);
        int count = count(lqw);
        return count > 0;
    }

    private boolean nickNameExist(String nickname) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getNickName, nickname);
        int count = count(lqw);
        return count > 0;
    }
}




