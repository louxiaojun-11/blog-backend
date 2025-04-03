package com.lxj.myblog.domain.dto;

import lombok.Data;

@Data
public class SearchBlogDTO {
    private String keyword;
    private Integer page;
    private Integer pageSize;

}
