package com.lxj.myblog.job;

import cn.hutool.core.collection.CollUtil;
import com.lxj.myblog.domain.dto.BlogEsDTO;
import com.lxj.myblog.domain.vo.BlogVO;
import com.lxj.myblog.esdao.BlogEsDao;
import com.lxj.myblog.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import java.util.List;
import java.util.stream.Collectors;
//若要启用全量同步,去除下方@Component注解即可
//@Component

/**
 * 全量同步博客到 ES
 */
@Slf4j
public class FullSyncBlogToEs implements CommandLineRunner {

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogEsDao blogEsDao;

    @Override
    public void run(String... args) {
        // 全量获取题目（数据量不大的情况下使用）
        List<BlogVO> blogVOList = blogService.list();
        if (CollUtil.isEmpty(blogVOList)) {
            return;
        }
        // 转为 ES 实体类
        List<BlogEsDTO> blogEsDTOList = blogVOList.stream()
                .map(BlogEsDTO::objToDto)
                .collect(Collectors.toList());
        // 分页批量插入到 ES
        final int pageSize = 500;
        int total = blogEsDTOList.size();
        log.info("FullSyncBlogToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize) {
            // 注意同步的数据下标不能超过总数据量
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            blogEsDao.saveAll(blogEsDTOList.subList(i, end));
        }
        log.info("FullSyncQuestionToEs end, total {}", total);
    }
}