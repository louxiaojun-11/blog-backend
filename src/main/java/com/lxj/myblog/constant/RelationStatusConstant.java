package com.lxj.myblog.constant;

/**
 * 用户关注状态常量
 */
public class RelationStatusConstant {

    //二者无互关关系
    public static final Integer NO_MUTUAL_FOLLOWING = 0;
    //当前用户单向关注关系用户
    public static final Integer CURRENT_FOLLOWING_RELATION = 1;
    //关系用户单向关注当前用户
    public static final Integer RELATION_FOLLOWING_CURRENT = 2;
    //二者互关
    public static final Integer MUTUAL_FOLLOWING = 3;
}
