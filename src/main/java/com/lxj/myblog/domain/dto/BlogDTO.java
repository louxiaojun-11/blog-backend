package com.lxj.myblog.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BlogDTO implements Serializable {
    private String title;
    private String content;
    private Integer userId;
}
