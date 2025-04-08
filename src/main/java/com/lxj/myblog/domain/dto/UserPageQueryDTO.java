package com.lxj.myblog.domain.dto;

import lombok.Data;

@Data
public class UserPageQueryDTO {
    private String username;
    private int page;
    private int pageSize;


}
