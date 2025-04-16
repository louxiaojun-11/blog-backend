package com.lxj.myblog.mapper;

import com.lxj.myblog.domain.dto.InformationDTO;
import com.lxj.myblog.domain.entity.ChatRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InformationMapper {
    @Insert("insert into information(title, content, admin_id, pic, type)" +
            " values (#{title}, #{content}, #{adminId}, #{pic}, #{type})")
    void releaseInformation(InformationDTO informationDTO);
    @Delete("delete from information where information_id = #{informationId}")
    void deleteInformation(String informationId);
}
