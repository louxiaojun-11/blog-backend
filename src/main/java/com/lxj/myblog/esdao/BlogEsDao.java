package com.lxj.myblog.esdao;

import com.lxj.myblog.domain.dto.BlogEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BlogEsDao extends ElasticsearchRepository<BlogEsDTO,Integer> {
    List<BlogEsDTO> findByUserId(Integer userId);
}
