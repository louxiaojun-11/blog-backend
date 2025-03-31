package com.lxj.myblog.domain.dto;

import lombok.Data;

@Data
public class HobbyGroupPageDTO {
    private Integer userId;
    private String groupName;
    private String type;
    private Integer page;
    private Integer pageSize;
}
