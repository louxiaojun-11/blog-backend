package com.lxj.myblog.mapper;

import com.github.pagehelper.Page;
import com.lxj.myblog.domain.dto.SensitiveWordDTO;
import com.lxj.myblog.domain.entity.SensitiveWord;
import org.apache.ibatis.annotations.*;

import java.util.Set;

@Mapper
public interface SensitiveWordMapper {
    @Select("SELECT word FROM sensitive_words")
    Set<String> findAllWords();
    
    @Insert("INSERT INTO sensitive_words(word) VALUES(#{word})")
    void insert(String word);
    
    @Delete("DELETE FROM sensitive_words WHERE id = #{id}")
    int deleteById(Integer id);

    Page<SensitiveWord> pageQuerySensitiveWords(SensitiveWordDTO sensitiveWordDTO);
}