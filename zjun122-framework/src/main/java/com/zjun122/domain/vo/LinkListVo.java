package com.zjun122.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkListVo {

    private Long id;

    private String name;

    private String logo;

    private String description;
    //网站地址
    private String address;
    //状态
    private String status;
}
