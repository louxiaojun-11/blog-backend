package com.lxj.myblog.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupBlogListVO {
    private Integer blogId;
    private String title;
    private String content;
    private String username;
    private Integer likes;
    private Integer comments;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
