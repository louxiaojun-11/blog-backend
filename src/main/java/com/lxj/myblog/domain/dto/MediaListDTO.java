package com.lxj.myblog.domain.dto;

import lombok.Data;

@Data
public class MediaListDTO {
    private Integer userId;
    private Integer page;
    private Integer pageSize;
}
