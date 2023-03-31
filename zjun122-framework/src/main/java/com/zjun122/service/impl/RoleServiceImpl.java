package com.zjun122.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjun122.constants.SystemConstants;
import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.dto.RoleChangeStatusDto;
import com.zjun122.domain.dto.RoleDto;
import com.zjun122.domain.entity.Role;
import com.zjun122.domain.vo.PageVo;
import com.zjun122.domain.vo.RoleListVo;
import com.zjun122.domain.vo.RoleVo;
import com.zjun122.service.RoleService;
import com.zjun122.mapper.RoleMapper;
import com.zjun122.utils.BeanCopyUtils;
import com.zjun122.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
* @author zjun122
* @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
* @createDate 2023-03-24 15:13:29
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if (SecurityUtils.isAdmin()) {
            List<String> list = new ArrayList<>();
            list.add("admin");
            return list;
        }
        //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult selectRoleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.hasText(roleName), Role::getRoleName, roleName);
        lqw.eq(StringUtils.hasText(status), Role::getStatus, status);
        lqw.orderByAsc(Role::getRoleSort);

        Page<Role> page = new Page<>(pageNum, pageSize);
        page(page, lqw);

        List<RoleListVo> vos = BeanCopyUtils.copyBeanList(page.getRecords(), RoleListVo.class);
        return ResponseResult.okResult(new PageVo(vos, page.getTotal()));
    }

    @Override
    public ResponseResult changeStatus(RoleChangeStatusDto roleChangeStatusDto) {
        if (roleChangeStatusDto.getStatus().equals("1") || roleChangeStatusDto.getStatus().equals("0")) {
            baseMapper.updateRoleStatus(roleChangeStatusDto.getRoleId(), roleChangeStatusDto.getStatus());
        }

        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addRole(RoleDto roleDto) {
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        save(role);

        baseMapper.addRoleAndMenu(role.getId(), roleDto.getMenuIds());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRole(Long id) {
        Role role = getById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    @Transactional
    public ResponseResult updateRole(RoleDto roleDto) {
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        updateById(role);

        baseMapper.delRoleAndMenu(role.getId());
        if (roleDto.getMenuIds() != null && roleDto.getMenuIds().size() > 0) {
            baseMapper.addRoleAndMenu(role.getId(), roleDto.getMenuIds());
        }
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult delRole(List<Long> id) {
        removeByIds(id);
/*        for (Long roleId : id) {
            baseMapper.delRoleAndMenu(roleId);
        }*/
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        return ResponseResult.okResult(list(lqw));
    }
}




