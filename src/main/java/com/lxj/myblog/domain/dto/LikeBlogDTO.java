package com.lxj.myblog.domain.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class LikeBlogDTO implements Serializable {
    private Integer userId;
    private Integer blogId;

}
