package com.lxj.myblog.mapper;

import com.lxj.myblog.domain.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {
    @Select("SELECT * FROM admin WHERE account = #{account}")
    Admin getByAccount(String account);
}
