package com.lxj.myblog.domain.dto;

import lombok.Data;

@Data
public class MusicListDTO {
    private Integer userId;
    private Integer page;
    private Integer pageSize;
}
