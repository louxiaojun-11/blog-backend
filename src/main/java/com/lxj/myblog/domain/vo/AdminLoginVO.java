package com.lxj.myblog.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AdminLoginVO implements Serializable {
    private Integer adminId;
    private String account;
    private String adminName;
    private String avatar;
    private String token;
}
