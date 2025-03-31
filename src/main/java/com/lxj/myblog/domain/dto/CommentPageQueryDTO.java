package com.lxj.myblog.domain.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class CommentPageQueryDTO implements Serializable {
    private int page;
    private int pageSize;
    private int blogId;
}
