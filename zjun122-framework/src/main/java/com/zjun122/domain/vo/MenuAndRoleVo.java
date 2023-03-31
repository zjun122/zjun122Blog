package com.zjun122.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuAndRoleVo {
    //菜单树
    private List<MenuTreeVo> menus;
    //角色所关联的菜单权限id列表
    private List<String> checkedKeys;
}
