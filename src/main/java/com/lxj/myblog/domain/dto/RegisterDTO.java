package com.lxj.myblog.domain.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private Integer userId;
    private String account;
    private String password;
    private String introduce;
}
