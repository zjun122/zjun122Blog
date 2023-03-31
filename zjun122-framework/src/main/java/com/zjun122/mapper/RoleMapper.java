package com.zjun122.mapper;

import com.zjun122.domain.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author zjun122
* @description 针对表【sys_role(角色信息表)】的数据库操作Mapper
* @createDate 2023-03-24 15:13:29
* @Entity com.zjun122.domain.entity.Role
*/
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户id查询角色的key
     * @param id
     * @return
     */
    List<String> selectRoleKeyByUserId(Long id);

    /**
     * 更改角色状态
     * @param roleId
     * @param status
     */
    void updateRoleStatus(@Param("id") Long roleId, @Param("status") String status);

    /**
     * 添加角色和菜单的关联关系
     * @param id
     * @param menuIds
     */
    void addRoleAndMenu(@Param("id") Long id, @Param("menuIds") List<String> menuIds);

    /**
     * 删除角色和菜单的关联关系
     * @param id
     */
    void delRoleAndMenu(Long id);
}




