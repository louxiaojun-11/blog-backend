package com.lxj.myblog.mapper;

import com.lxj.myblog.domain.entity.Admin;
import com.lxj.myblog.domain.response.ApiResponse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface AdminMapper {
    @Select("SELECT * FROM admin WHERE account = #{account}")
    Admin getByAccount(String account);
    @Insert("insert into notice (user_id,content,type,admin_id) values(#{userId}, #{content}, #{type}, #{adminId})")
    void sendNotice(@Param("userId")Integer userId, @Param("content")String content,
                    @Param("type")String type, @Param("adminId")Integer adminId,
                    @Param("operationUserId") Integer operationUserId);
   @Select("select user_id from users")
    List<Integer> getAllUserIds();
   @Select("select user_id from users where status = 'online'")
    List<Integer> getOnlineUserIds();
   @Select("  SELECT COUNT(*) FROM blog_post WHERE created_at BETWEEN #{startOfDay} AND #{endOfDay}")
   int countTodayBlogAmount(@Param("startOfDay") Date startOfDay, @Param("endOfDay") Date endOfDay);
   @Select("SELECT COUNT(*) FROM users WHERE created_at BETWEEN #{startOfDay} AND #{endOfDay}")
    Integer countTodayUser(@Param("startOfDay") Date startOfDay, @Param("endOfDay") Date endOfDay);
   @Select("SELECT COUNT(*) FROM users WHERE status = 'online'")
    Integer countOnlineUser();
    @Select("SELECT COUNT(*) FROM users")
    Integer countAllUsers();
}
