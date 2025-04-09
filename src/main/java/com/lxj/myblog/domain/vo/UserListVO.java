package com.lxj.myblog.domain.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserListVO {
    private Integer  userId;

    private String username;

    private String account;

    private String avatar;


    private UserStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastActive;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;


    public enum UserStatus {
        online, offline
    }
}
