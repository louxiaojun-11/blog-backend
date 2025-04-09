package com.lxj.myblog.domain.dto;

import lombok.Data;

@Data
public class UserViolationDTO {
    private Integer userId;
    private Boolean avatarLegal;
    private Boolean usernameLegal;

}
