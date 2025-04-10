package com.lxj.myblog.mapper;

import com.github.pagehelper.Page;
import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.entity.User;
import com.lxj.myblog.domain.vo.MusicVO;
import com.lxj.myblog.domain.vo.UserListVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
@Repository
public interface UserMapper {


    @Select("SELECT u.* FROM users u " +
            "JOIN friendships f ON u.user_id = f.friend_id " +
            "WHERE f.user_id = #{userId} AND f.status = 'accepted'")
    List<User> findFriendsByUserId(Integer userId);

//    @Update("UPDATE users SET status = #{status}, last_active = #{lastActive} " +
//            "WHERE id = #{id}")
//    void updateUserStatus(@Param("id") Long id,
//                          @Param("status") String status,
//                          @Param("lastActive") LocalDateTime lastActive);

    @Select("SELECT * FROM users WHERE account = #{account}")
    User getByAccount(String account);

    @Select("SELECT * FROM users WHERE user_id = #{userId}")
    User getByUserId(Integer userId);

    void update(UserInfoDTO userInfoDTO);

    @Select("SELECT password FROM users WHERE user_id = #{userId}")
    String getPasswordByUserId(Integer userId);

    @Select("SELECT COUNT(*) AS count FROM user_follower WHERE following_id = #{userId}")
    Integer getFollowerAmount(Integer userId);

    @Select("SELECT COUNT(*) AS count FROM user_follower WHERE user_id = #{userId}")
    Integer getFollowingAmount(Integer userId);

    @Select("select introduce from user_introduce where user_id = #{userId}")
    String getIntroduce(Integer userId);

    @Insert("INSERT INTO user_introduce (user_id, introduce)\n" +
            "VALUES (#{userId}, #{introduce})\n" +
            "ON DUPLICATE KEY UPDATE introduce = #{introduce};")
    void updateIntroduce(IntroduceDTO introduceDTO);

    void register(RegisterDTO registerDTO);

    @Select("select * from user_music where user_id = #{userId} order by created_at desc")
    Page<MusicVO> musicPageQuery(MusicListDTO musicListDTO);

    @Insert("INSERT INTO user_music (user_id, music_url,music_name) VALUES (#{userId}, #{musicURL},#{musicName})")
    void uploadMusic(@Param("userId") Integer userId, @Param("musicURL") String musicURL, @Param("musicName") String musicName);

    @Update("UPDATE users SET status = #{status} WHERE user_id = #{senderId}")
    void updateUserStatus(@Param("senderId") Integer senderId, @Param("status") String status);


    Page<UserListVO> getUserList(UserPageQueryDTO userPageQueryDTO);
    @Update("UPDATE users SET avatar = #{defaultAvatar} WHERE user_id = #{userId}")
    void updateUserAvatar(@Param("userId")Integer userId, @Param("defaultAvatar")String defaultAvatar);
    @Update("UPDATE users SET username = #{result} WHERE user_id = #{userId}")
    void updateUsername(@Param("userId") Integer userId, @Param("result") String result);
   @Select("SELECT username FROM users WHERE user_id = #{userId}")
    String getUsernameById(Integer userId);
}