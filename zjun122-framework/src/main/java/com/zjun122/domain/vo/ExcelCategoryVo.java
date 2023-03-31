package com.zjun122.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelCategoryVo {
    @ExcelProperty("分类名")
    private String name;
    @ExcelProperty("描述")
    private String description;
    @ExcelProperty("状态：0正常,1禁用")
    private String status;
}
