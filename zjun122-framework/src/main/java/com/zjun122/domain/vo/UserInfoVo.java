package com.zjun122.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserInfoVo {
    private Long id;
    //用户昵称
    private String nickName;
    //用户性别
    private String sex;
    //用户头像
    private String avatar;
    //用户邮箱
    private String email;
}
