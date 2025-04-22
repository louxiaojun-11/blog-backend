package com.lxj.myblog.domain.vo;

import lombok.Data;

@Data
public class RecommendedUserVO {
  private Integer userId;
  private String username;
  private String avatar;
  private Integer followerAmount;
}


