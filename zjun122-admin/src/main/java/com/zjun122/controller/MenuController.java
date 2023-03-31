package com.zjun122.controller;

import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.entity.Menu;
import com.zjun122.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult list(String menuName, String status) {
        return menuService.selectMenuList(menuName, status);
    }

    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu) {
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getMenu(@PathVariable Long id) {
        return menuService.getMenu(id);
    }

    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu) {
        return menuService.updateMenu(menu);
    }

    @DeleteMapping("/{id}")
    public ResponseResult delMenu(@PathVariable Long id) {
        return menuService.delMenu(id);
    }

    @GetMapping("/treeselect")
    public ResponseResult treeSelect() {
        return menuService.treeSelect();
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeSelect(@PathVariable Long id) {
        return menuService.roleMenuTreeSelect(id);
    }
}
