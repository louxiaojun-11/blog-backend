package com.lxj.myblog.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxj.myblog.domain.vo.AuthorVO;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Blog {
    private Integer id;
    private String title;
    private String content;
    private Integer userId;
    private Integer likes;
    private Integer views;
    private Integer comments;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
