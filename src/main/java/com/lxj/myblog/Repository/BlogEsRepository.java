package com.lxj.myblog.Repository;

import com.lxj.myblog.domain.vo.BlogVO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BlogEsRepository extends ElasticsearchRepository<BlogVO, String> {
}
