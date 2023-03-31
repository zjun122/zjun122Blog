package com.zjun122.domain.vo;

import com.zjun122.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateVo {
    private UserVo user;
    private List<Long> roleIds;
    private List<Role> roles;
}
