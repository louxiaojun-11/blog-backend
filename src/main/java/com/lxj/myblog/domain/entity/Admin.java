package com.lxj.myblog.domain.entity;


import lombok.Data;

@Data
public class Admin {
    private Integer  adminId;

    private String adminName;

    private String avatar;

    private String account;

    private String password;

    private AdminStatus status = AdminStatus.activate;
    public enum AdminStatus {
        activate, deactivate
    }

}
