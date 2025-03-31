package com.lxj.myblog.domain.vo;

import lombok.Data;

@Data
public class HobbyGroupVO {
    private Integer groupId;
    private String groupName;
    private String avatar;
    private String introduce;
    private Integer members;
    private String type;
}
