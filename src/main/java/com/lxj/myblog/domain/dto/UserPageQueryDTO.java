package com.lxj.myblog.domain.dto;

import lombok.Data;

@Data
public class UserPageQueryDTO {
    private String key;
    private int page;
    private int pageSize;
    //timeOrder true:时间从晚到早 false：时间从早到晚
    private Boolean timeOrder ;


}
