package com.lxj.myblog.domain.dto;

import lombok.Data;

@Data
public class InformationPageDTO {
    private String type;
    private Integer page;
    private Integer pageSize;
}
