package com.lxj.myblog.domain.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserLoginDTO implements Serializable {

    private String account;
    private String password;
}
