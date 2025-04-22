package com.lxj.myblog.domain.dto;

import lombok.Data;

@Data
public class ReviewGroupDTO {
    private Integer groupId;
    private String groupName;
    private Integer userId;
    private String result;
}
