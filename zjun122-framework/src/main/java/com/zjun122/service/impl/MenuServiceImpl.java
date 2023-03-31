package com.zjun122.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjun122.constants.SystemConstants;
import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.entity.Menu;
import com.zjun122.domain.vo.*;
import com.zjun122.mapper.MenuMapper;
import com.zjun122.service.MenuService;
import com.zjun122.utils.BeanCopyUtils;
import com.zjun122.utils.SecurityUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zjun122
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
 * @createDate 2023-03-24 15:13:05
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
        implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果用户id为1，那么就代表为管理员，直接返回全部权限
        if (id == 1L) {
            LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
            lqw.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            lqw.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(lqw);
            List<String> perms = menus.parallelStream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }

        //如果用户id不为1，则去查找对应的权限信息
        return getBaseMapper().selectPermByUserId(id);
    }

    @Override
    public ResponseResult selectRouterMenuTreeByUserId() {
        //从Security中获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //获取mapper接口
        MenuMapper menuMapper = getBaseMapper();
        //定义一个menu集合，接收参数
        List<Menu> menus = null;

        //判断是否为管理员
        if (SecurityUtils.isAdmin()) {
            //如果为管理员，直接获取全部路由信息
            menus = menuMapper.selectAllRouterMenu();
        } else {
            //如果不为管理，则根据id查询对应的路由信息
            menus = menuMapper.selectRouterTreeByUserId(userId);
        }

        //1、将menu集合信息转换成vo对象
        //2、将menus和所有最大的父级menuId传入到构建树形菜单集合方法中
        List<MenuVo> menuTree = builderMenuTree(BeanCopyUtils.copyBeanList(menus, MenuVo.class), 0L);

/*        List<Menu> parentNode = menus.parallelStream()
                .filter(menu -> menu.getParentId() == 0)
                .collect(Collectors.toList());

        for (Menu menu : parentNode) {
            menu.setChildren(menus.parallelStream()
                    .filter(menuVo -> menu.getId().equals(menuVo.getParentId()))
                    .map(menuVo -> menuVo.setChildren(getChildrenList(menuVo, menuVos)))
                    .collect(Collectors.toList()));
        }*/

        return ResponseResult.okResult(new RoutersVo(menuTree));
    }

    @Override
    public ResponseResult selectMenuList(String menuName, String status) {
        LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.hasText(menuName), Menu::getMenuName, menuName);
        lqw.eq(StringUtils.hasText(status), Menu::getStatus, status);
        lqw.orderByAsc(Menu::getOrderNum, Menu::getId);

        List<MenuListVo> listVos = BeanCopyUtils.copyBeanList(list(lqw), MenuListVo.class);
        return ResponseResult.okResult(listVos);
    }

    @Override
    public ResponseResult getMenu(Long id) {
        Menu menu = getById(id);
        MenuOneVo menuOneVo = BeanCopyUtils.copyBean(menu, MenuOneVo.class);
        return ResponseResult.okResult(menuOneVo);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if (Objects.equals(menu.getId(), menu.getParentId())) {
            throw new RuntimeException("修改菜单’" + menu.getMenuName() + "‘失败，上级菜单不能选择自己");
        }
        saveOrUpdate(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delMenu(Long id) {

        boolean flag = list().parallelStream()
                .anyMatch(menu -> Objects.equals(menu.getParentId(), id));
        if (flag) {
            throw new RuntimeException("存在子菜单，不允许删除");
        }

        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult treeSelect() {
        List<Menu> menus = baseMapper.selectAllRouterMenu();
        List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(menus, MenuTreeVo.class);

        return ResponseResult.okResult(getMenuTreeVos(menuTreeVos, menus));
    }

    @Override
    public ResponseResult roleMenuTreeSelect(Long id) {
        List<Menu> menus = baseMapper.selectAllRouterMenu();
        List<MenuTreeVo> menuTreeVos = getMenuTreeVos(BeanCopyUtils.copyBeanList(menus, MenuTreeVo.class), menus);
        List<String> checkedKeys = baseMapper.selectMenuIdByRole(id);

        return ResponseResult.okResult(new MenuAndRoleVo(menuTreeVos, checkedKeys));
    }

    /**
     * 获取角色列表中的树形菜单
     * @param menuTreeVos
     * @param menus
     * @return
     */
    private List<MenuTreeVo> getMenuTreeVos(List<MenuTreeVo> menuTreeVos, List<Menu> menus) {
        for (MenuTreeVo treeVo : menuTreeVos) {
            menus.stream()
                    .map(menu ->  {
                        if (menu.getId() == treeVo.getId()) {
                            treeVo.setLabel(menu.getMenuName());
                        }
                        return menu;
                    })
                    .collect(Collectors.toList());
        }

        List<MenuTreeVo> parentNode = menuTreeVos.parallelStream()
                .filter(menuTreeVo -> menuTreeVo.getParentId() == 0L)
                .map(menuTreeVo -> menuTreeVo.setChildren(getMenuTreeVosChildren(menuTreeVo, menuTreeVos)))
                .collect(Collectors.toList());
        return parentNode;
    }

    /**
     * 根据路由集合信息，获取路由的子集合 menuTreeVos
     * @param menu
     * @param menus
     * @return
     */
    private List<MenuTreeVo> getMenuTreeVosChildren(MenuTreeVo menu, List<MenuTreeVo> menus) {
        List<MenuTreeVo> childrenList = menus.parallelStream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getMenuTreeVosChildren(m, menus)))
                .collect(Collectors.toList());
        return childrenList;
    }

    /**
     * 构建树形菜单集合
     * @param menus
     * @param parentId
     * @return
     */
    private List<MenuVo> builderMenuTree(List<MenuVo> menus, Long parentId) {
        List<MenuVo> menuTree = menus.parallelStream()
                //过滤全部出全部父级菜单的信息
                .filter(menu -> menu.getParentId().equals(parentId))
                //将子菜单转入到对应的父级菜单中
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 根据路由集合信息，获取路由的子集合
     * @param menu
     * @param menus
     * @return
     */
    private List<MenuVo> getChildren(MenuVo menu, List<MenuVo> menus) {
        List<MenuVo> childrenList = menus.parallelStream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}




