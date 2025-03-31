package com.lxj.myblog.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileVO implements Serializable {
    private Integer following;
    private Integer follower;
    private String introduce;

}
