package com.zjun122.service;

import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author zjun122
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
* @createDate 2023-03-24 15:13:05
*/
public interface MenuService extends IService<Menu> {

    /**
     * 根据用户id查询用户菜单的权限信息
     * @param id
     * @return
     */
    List<String> selectPermsByUserId(Long id);

    /**
     * 查询并构建树形的菜单信息
     * （因为获取userId在service的实现类中，所以controller层不需要传入userId
     * @return
     */
    ResponseResult selectRouterMenuTreeByUserId();

    ResponseResult selectMenuList(String menuName, String status);

    ResponseResult getMenu(Long id);

    ResponseResult updateMenu(Menu menu);

    ResponseResult delMenu(Long id);

    ResponseResult treeSelect();

    ResponseResult roleMenuTreeSelect(Long id);
}
