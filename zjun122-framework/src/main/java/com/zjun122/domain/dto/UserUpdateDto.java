package com.zjun122.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
    private Long id;
    //用户名称
    private String userName;
    //用户昵称
    private String nickName;
    //用户性别
    private String sex;
    //用户邮箱
    private String email;
    //状态
    private String status;
    //手机号
    private String phonenumber;
    //用户的角色信息
    private List<Long> roleIds;
}
