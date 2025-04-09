package com.lxj.myblog.domain.entity;

import lombok.Data;

@Data
public class BlogViolationRecord {
    private Integer userId;
    private Integer blogId;
    private String reason;
    private Integer adminId;
    private String title;
    private String content;


}
