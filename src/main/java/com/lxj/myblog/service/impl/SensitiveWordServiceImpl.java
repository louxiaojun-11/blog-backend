package com.lxj.myblog.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lxj.myblog.domain.dto.SensitiveWordDTO;
import com.lxj.myblog.domain.entity.Blog;
import com.lxj.myblog.domain.entity.SensitiveWord;
import com.lxj.myblog.domain.response.SensitiveCheckResult;
import com.lxj.myblog.fliter.SensitiveWordFilter;
import com.lxj.myblog.mapper.SensitiveWordMapper;
import com.lxj.myblog.result.PageResult;
import com.lxj.myblog.service.SensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class SensitiveWordServiceImpl implements SensitiveWordService{

     @Autowired
    SensitiveWordMapper sensitiveWordMapper;

    // 使用volatile保证多线程可见性
    private volatile SensitiveWordFilter filter;

    @PostConstruct
    public void init() {
        reloadFilter();
    }

    // 重新加载敏感词库
    public void reloadFilter() {
        Set<String> words = sensitiveWordMapper.findAllWords();
        this.filter = new SensitiveWordFilter(words);
    }

    // 添加敏感词
    public void addWord(String word) {
        sensitiveWordMapper.insert(word);
        reloadFilter(); // 重新加载
    }

    // 删除敏感词
    public void deleteWord(Integer id) {
        sensitiveWordMapper.deleteById(id);
        reloadFilter(); // 重新加载
    }

    // 检测文本
    public SensitiveCheckResult checkText(String text) {
        boolean contains = filter.containsSensitiveWord(text);
        Set<String> words = contains ? filter.getSensitiveWords(text) : Collections.emptySet();
        return new SensitiveCheckResult(contains, words);
    }

    @Override
    public PageResult pageQuerySensitiveWords(SensitiveWordDTO sensitiveWordDTO) {
        PageHelper.startPage(sensitiveWordDTO.getPage(),sensitiveWordDTO.getPageSize());
        Page<SensitiveWord> pageQuery = sensitiveWordMapper.pageQuerySensitiveWords(sensitiveWordDTO);
        long total = pageQuery.getTotal();
        List<SensitiveWord> records = pageQuery.getResult();
        return new PageResult(total, records);
    }
}