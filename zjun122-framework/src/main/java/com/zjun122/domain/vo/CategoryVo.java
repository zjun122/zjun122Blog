package com.zjun122.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVo {
    //id
    private Long id;
    //分类名字
    private String name;
    //描述信息
    private String description;
}
