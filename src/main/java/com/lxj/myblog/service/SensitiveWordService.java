package com.lxj.myblog.service;

import com.lxj.myblog.domain.dto.SensitiveWordDTO;
import com.lxj.myblog.domain.response.SensitiveCheckResult;
import com.lxj.myblog.fliter.SensitiveWordFilter;
import com.lxj.myblog.result.PageResult;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Set;

public interface SensitiveWordService {

    // 重新加载敏感词库
    public void reloadFilter();
    // 添加敏感词
    public void addWord(String word);

    // 删除敏感词
    public void deleteWord(Integer id);

    // 检测文本
    public SensitiveCheckResult checkText(String text);

    PageResult pageQuerySensitiveWords(SensitiveWordDTO sensitiveWordDTO);
}
