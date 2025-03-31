package com.lxj.myblog.domain.dto;

import lombok.Data;

@Data
public class GroupBlogListPageDTO {
    private Integer groupId;
    private Integer userId;
    private Integer page;
    private Integer pageSize;
}
