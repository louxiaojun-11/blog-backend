package com.lxj.myblog.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ChatRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer senderId;
    private Integer receiverId;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;
}