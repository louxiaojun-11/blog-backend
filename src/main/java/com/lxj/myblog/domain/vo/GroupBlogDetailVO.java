package com.lxj.myblog.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupBlogDetailVO {
    private String groupId;
    private String blogId;
    private String userId;
    private String title;
    private String content;
    private String pic1;
    private String pic2;
    private String pic3;
    private String pic4;
    private Integer likes;
    private Integer comments;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
