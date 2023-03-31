package com.zjun122.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    //用户名
    private String userName;
    //用户昵称
    private String nickName;
    //密码
    private String password;
    //用户性别
    private String sex;
    //用户头像
    private String avatar;
    //用户邮箱
    private String email;
    //角色id集合
    private List<Long> roleIds;
}
