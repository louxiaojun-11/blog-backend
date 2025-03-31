package com.lxj.myblog.domain.vo;

import lombok.Data;

@Data
public class GroupInfoVO {
    private String groupName;
    private Integer members;
    private String introduce;
    private String avatar;
    private String type;
    private Integer status;
}
