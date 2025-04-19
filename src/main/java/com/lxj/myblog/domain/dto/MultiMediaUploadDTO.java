package com.lxj.myblog.domain.dto;

import lombok.Data;

@Data
public class MultiMediaUploadDTO {
    private Integer userId;
    private String url;
    private String fileName;
    private String type;
}
