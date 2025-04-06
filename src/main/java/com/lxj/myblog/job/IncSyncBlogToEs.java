package com.lxj.myblog.job;

import cn.hutool.core.collection.CollUtil;
import com.lxj.myblog.domain.dto.BlogEsDTO;
import com.lxj.myblog.domain.vo.BlogVO;
import com.lxj.myblog.esdao.BlogEsDao;
import com.lxj.myblog.mapper.BlogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;

/**
 * 增量同步博客到ES
 */
@Component
@Slf4j
public class IncSyncBlogToEs  {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private  BlogEsDao blogEsDao;

    /**
     * 每20秒执行一次
     */
    @Scheduled(fixedRate = 20 * 1000)
    public void run() {
        // 查询近 40秒内的数据
        long updateTime =  40 * 1000L;
        Date fortySecondsAgoDate = new Date(new Date().getTime() - updateTime);
        List<BlogVO> blogVOList = blogMapper.listQuestionWithDelete(fortySecondsAgoDate);
        blogVOList.forEach(blogVO -> blogVO.setAuthor(blogMapper.getAuthor(blogVO.getUserId())));
        if (CollUtil.isEmpty(blogVOList)) {
            log.info("no inc blog");
            return;
        }
        List<BlogEsDTO> blogEsDTOList = blogVOList.stream()
                .map(BlogEsDTO::objToDto)
                .collect(Collectors.toList());
        final int pageSize = 500;
        int total = blogEsDTOList.size();
        log.info("IncSyncQuestionToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize) {
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            blogEsDao.saveAll(blogEsDTOList.subList(i, end));
        }
        log.info("IncSyncBlogToEs end, total {}", total);
    }
}
