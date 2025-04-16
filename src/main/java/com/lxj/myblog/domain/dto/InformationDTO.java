package com.lxj.myblog.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class InformationDTO implements Serializable {
    private String title;
    private String content;
    private Integer adminId;
    private String pic;
    private String type;

}
