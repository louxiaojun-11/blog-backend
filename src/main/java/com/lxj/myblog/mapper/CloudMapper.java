package com.lxj.myblog.mapper;

import com.github.pagehelper.Page;
import com.lxj.myblog.domain.dto.MediaListDTO;
import com.lxj.myblog.domain.dto.MultiMediaUploadDTO;
import com.lxj.myblog.domain.vo.MultiMediaVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CloudMapper {
    @Select("SELECT * FROM user_multimediafile WHERE type = 'music' AND user_id = #{userId} ORDER BY created_at DESC")
    Page<MultiMediaVO> musicPageQuery(MediaListDTO mediaListDTO);
    @Select("SELECT * FROM user_multimediafile WHERE type = 'video' AND user_id = #{userId} ORDER BY created_at DESC")
    Page<MultiMediaVO> videoPageQuery(MediaListDTO mediaListDTO);
    @Select("SELECT * FROM user_multimediafile WHERE type = 'photo' AND user_id = #{userId} ORDER BY created_at DESC")
    Page<MultiMediaVO> photoPageQuery(MediaListDTO mediaListDTO);
    @Insert("INSERT INTO user_multimediafile (user_id,file_name, url, type) VALUES (#{userId}, #{fileName}, #{url}, #{type})")
    void uploadMultiMediaFile(MultiMediaUploadDTO multiMediaFile);
    @Delete("DELETE FROM user_multimediafile WHERE file_id = #{fileId}")
    void deleteFile(Integer fileId);
}
