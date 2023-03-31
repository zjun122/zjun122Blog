package com.zjun122.mapper;

import com.zjun122.domain.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author zjun122
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
* @createDate 2023-03-24 15:13:05
* @Entity com.zjun122.domain.entity.Menu
*/
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据id查询perms信息
     * @param id
     * @return
     */
    List<String> selectPermByUserId(Long id);

    /**
     * 返回所有的路由菜单信息
     * @return
     */
    List<Menu> selectAllRouterMenu();

    /**
     * 根据id查询对应的路由菜单信息
     * @param userId
     * @return
     */
    List<Menu> selectRouterTreeByUserId(Long userId);

    /**
     * 根据角色id，查询路由树级关系信息
     * @param id
     * @return
     */
    List<Menu> selectRouterTreeByRoleId(Long id);

    /**
     * 根据角色id查询菜单
     * @param id
     * @return
     */
    List<String> selectMenuIdByRole(Long id);
}




