package com.lxj.myblog.mapper;

import com.lxj.myblog.domain.entity.Admin;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {
    @Select("SELECT * FROM admin WHERE account = #{account}")
    Admin getByAccount(String account);
    @Insert("insert into notice (user_id,content,type,admin_id) values(#{userId}, #{content}, #{type}, #{adminId})")
    void sendNotice(@Param("userId")Integer userId, @Param("content")String content,
                    @Param("type")String type, @Param("adminId")Integer adminId,
                    @Param("operationUserId") Integer operationUserId);
}
