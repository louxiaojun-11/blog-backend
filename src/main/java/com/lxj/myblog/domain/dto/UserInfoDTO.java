package com.lxj.myblog.domain.dto;

import lombok.Data;

@Data
public class UserInfoDTO {
    private Integer userId;
    private String username;
    private String avatar;
    private String password;
}
