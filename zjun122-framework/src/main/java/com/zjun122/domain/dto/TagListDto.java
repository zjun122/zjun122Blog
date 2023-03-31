package com.zjun122.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagListDto {
    //标签名
    private String name;
    //备注
    private String remark;
}
