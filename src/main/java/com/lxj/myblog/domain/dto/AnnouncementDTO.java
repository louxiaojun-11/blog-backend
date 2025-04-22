package com.lxj.myblog.domain.dto;

import lombok.Data;

@Data
public class AnnouncementDTO {
    private String content;
    private Integer adminId;
    private String target;
}
