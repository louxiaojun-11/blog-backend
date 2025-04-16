package com.lxj.myblog.domain.dto;

import lombok.Data;

@Data
public class SensitiveWordDTO {
    private String keyword;
    private Integer page;
    private Integer pageSize;
}
