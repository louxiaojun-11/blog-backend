package com.lxj.myblog.domain.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class CommentDTO implements Serializable {
   private Integer blogId;
   private Integer userId;
   private String content;
}
