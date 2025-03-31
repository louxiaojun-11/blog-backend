package com.lxj.myblog.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Integer id;
    private Integer blogId;
    private String content;
    private Integer userId;
    private String username;
    private String avatar;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

}
