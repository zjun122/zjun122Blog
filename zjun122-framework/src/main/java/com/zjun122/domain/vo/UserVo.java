package com.zjun122.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    private Long id;
    //用户名称
    private String userName;
    //用户昵称
    private String nickName;
    //用户性别
    private String sex;
    //用户邮箱
    private String email;
    //手机号
    private String phonenumber;
    //状态
    private String status;
}
