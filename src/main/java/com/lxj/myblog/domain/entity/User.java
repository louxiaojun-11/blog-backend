package com.lxj.myblog.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {

    private Integer  userId;

    private String username;

    private String avatar;

    private UserStatus status = UserStatus.offline;

    private LocalDateTime lastActive;

    private String account;

   private String password;

    public enum UserStatus {
        online, offline
    }
}