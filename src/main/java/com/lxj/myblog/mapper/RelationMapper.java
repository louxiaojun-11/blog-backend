package com.lxj.myblog.mapper;

import com.lxj.myblog.domain.vo.RelationVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RelationMapper {
    @Select("SELECT COUNT(*) AS count FROM user_follower WHERE user_id = #{userId} AND following_id = #{relationId} ")
    Integer countCurrentFollow(@Param("userId") Integer userId, @Param("relationId") Integer relationId);

    @Select("SELECT COUNT(*) AS count FROM user_follower WHERE user_id = #{relationId} AND following_id = #{userId}")
    Integer countRelationFollow(@Param("userId") Integer userId, @Param("relationId") Integer relationId);

    //获取粉丝列表
    @Select("SELECT u_f.user_id,u.avatar,u.username " +
            "from user_follower u_f " +
            "left join users u on u_f.user_id =u.user_id " +
            "where u_f.following_id = #{userId} " +
            "order by u_f.follow_time desc")
    List<RelationVO> getFollowerList(Integer userId);

    //获取关注列表
    @Select("SELECT u_f.following_id,u.avatar,u.username " +
            "from user_follower u_f " +
            "left join users u on u_f.following_id =u.user_id " +
            "where u_f.user_id = #{userId} " +
            "order by u_f.follow_time desc")
    List<RelationVO> getFollowingList(Integer userId);

    @Select("select user_id,avatar,username from users where username like concat('%',#{searchContent},'%') ")
    List<RelationVO> getSearchList(@Param("searchContent") String searchContent);

    @Insert("INSERT INTO user_follower(user_id, following_id) values (#{userId},#{relationId})")
    void followRelation(@Param("userId") Integer userId, @Param("relationId") Integer relationId);

    @Select("SELECT COUNT(*) from user_follower where user_id = #{relationId} and following_id = #{userId}")
    int ifFriend(@Param("userId") Integer userId, @Param("relationId") Integer relationId);

    @Insert("INSERT INTO friendships(user_id, friend_id) values (#{userId},#{relationId}),(#{relationId},#{userId})")
    void addFriend(@Param("userId") Integer userId, @Param("relationId") Integer relationId);

    @Delete("DELETE from user_follower where user_id = #{userId} and following_id = #{relationId} ")
    void cancelFollow(@Param("userId") Integer userId, @Param("relationId") Integer relationId);
    @Delete("DELETE from friendships where " +
            "(user_id = #{userId} and friend_id = #{relationId}) " +
            "or (user_id = #{relationId} and friend_id = #{userId})")
    void cancelFriend(@Param("userId") Integer userId, @Param("relationId") Integer relationId);
}