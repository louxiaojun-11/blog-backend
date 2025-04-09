package com.lxj.myblog.domain.dto;

import lombok.Data;

@Data
public class UserBlogViolationDTO {
    private Integer blogId;
    private String reason;

}
