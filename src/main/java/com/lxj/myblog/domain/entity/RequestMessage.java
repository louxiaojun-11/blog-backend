package com.lxj.myblog.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 前端发给后端的消息格式
 */
@Data
public class RequestMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer receiverId;
    private String msg;
}
