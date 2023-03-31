package com.zjun122.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.dto.CategoryDto;
import com.zjun122.domain.entity.Category;
import com.zjun122.domain.vo.CategoryListVo;
import com.zjun122.domain.vo.ExcelCategoryVo;
import com.zjun122.enums.AppHttpCodeEnum;
import com.zjun122.service.CategoryService;
import com.zjun122.utils.BeanCopyUtils;
import com.zjun122.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    // 直接修改状态的接口没写

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory() {
        return categoryService.listAllCategory();
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
//            e.printStackTrace();
            //重置response
            response.reset();
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, String name, String status) {
        return categoryService.pageList(pageNum, pageSize, name, status);
    }

    @PostMapping()
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto) {
        categoryService.save(BeanCopyUtils.copyBean(categoryDto, Category.class));
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getCategory(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        return ResponseResult.okResult(BeanCopyUtils.copyBean(category, CategoryListVo.class));
    }

    @PutMapping
    public ResponseResult updateCategory(@RequestBody CategoryListVo categoryListVo) {
        categoryService.updateById(BeanCopyUtils.copyBean(categoryListVo, Category.class));
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult delCategory(@PathVariable List<Long> id) {
        categoryService.removeByIds(id);
        return ResponseResult.okResult();
    }
}
