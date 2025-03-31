package com.lxj.myblog.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RelationVO implements Serializable {
    private Integer relationId;
    private String username;
    private String avatar;
}
