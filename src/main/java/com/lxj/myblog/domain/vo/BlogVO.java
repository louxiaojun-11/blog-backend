package com.lxj.myblog.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class BlogVO {
    private Integer id;
    private String title;
    private String content;
    private Integer userID;
    private Integer likes;
    private Integer views;
    private Integer comments;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private  AuthorVO author = new AuthorVO();
}
