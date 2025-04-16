package com.lxj.myblog.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SensitiveWord {
    private Integer id;
    private String word;

}