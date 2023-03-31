package com.zjun122.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleChangeStatusDto {
    private Long roleId;
    private String status;
}
