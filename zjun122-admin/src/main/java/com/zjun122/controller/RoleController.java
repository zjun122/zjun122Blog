package com.zjun122.controller;

import cn.hutool.log.Log;
import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.dto.RoleChangeStatusDto;
import com.zjun122.domain.dto.RoleDto;
import com.zjun122.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, String roleName, String status) {
        return roleService.selectRoleList(pageNum, pageSize, roleName, status);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleChangeStatusDto roleChangeStatusDto) {
        return roleService.changeStatus(roleChangeStatusDto);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody RoleDto roleDto) {
        roleDto.setId(null);
        return roleService.addRole(roleDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getRole(@PathVariable Long id) {
        return roleService.getRole(id);
    }

    @PutMapping
    public ResponseResult updateRole(@RequestBody RoleDto roleDto) {
        return roleService.updateRole(roleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult delRole(@PathVariable List<Long> id) {
        return roleService.delRole(id);
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole() {
        return roleService.listAllRole();
    }

}
