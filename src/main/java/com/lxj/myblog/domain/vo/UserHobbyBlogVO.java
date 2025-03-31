package com.lxj.myblog.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserHobbyBlogVO {
    private Integer userId;
    private Integer groupId;
    private Integer blogId;
    private String groupName;
    private String title;
    private String content;
    private Integer likes;
    private Integer comments;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String type;
}
