package com.lxj.myblog.domain.vo;

import com.lxj.myblog.domain.entity.Blog;
import com.lxj.myblog.result.PageResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelationProfileVO implements Serializable {
    private Integer relationId;
    private String username;
    private String avatar;
    private Integer following;
    private Integer follower;
    private String introduce;
    //status:0:代表当前用户和关系用户没有关注关系;1,代表当前用户单向关注了关系用户;2,代表关系用户单向关注了当前用户;3:代表二者互关
    private Integer status;
    private PageResult pageResult;
}
