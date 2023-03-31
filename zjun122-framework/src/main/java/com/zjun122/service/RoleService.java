package com.zjun122.service;

import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.dto.RoleChangeStatusDto;
import com.zjun122.domain.dto.RoleDto;
import com.zjun122.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author zjun122
* @description 针对表【sys_role(角色信息表)】的数据库操作Service
* @createDate 2023-03-24 15:13:29
*/
public interface RoleService extends IService<Role> {
    /**
     * 根据用户id查询用户的角色
     * @param id
     * @return
     */
    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult selectRoleList(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeStatus(RoleChangeStatusDto roleChangeStatusDto);

    ResponseResult addRole(RoleDto roleDto);

    ResponseResult getRole(Long id);

    ResponseResult updateRole(RoleDto roleDto);

    ResponseResult delRole(List<Long> id);

    ResponseResult listAllRole();
}
