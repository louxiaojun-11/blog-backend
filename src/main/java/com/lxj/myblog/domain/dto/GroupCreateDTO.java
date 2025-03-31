package com.lxj.myblog.domain.dto;

import lombok.Data;

@Data
public class GroupCreateDTO {
    private String groupName;
    private Integer groupId;
    private Integer userId;
    private String introduce;
    private String avatar;
    private String type;

}
