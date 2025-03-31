package com.lxj.myblog.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
@Data
@Builder
public class UserLoginVO implements Serializable {
    private Integer userId;
    private String account;
    private String username;
    private String avatar;
    private String token;
}
