package com.lxj.myblog.domain.response;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * 敏感词检测结果封装类
 */
public class SensitiveCheckResult {
    private final boolean containsSensitiveWord; // 是否包含敏感词
    private final Set<String> sensitiveWords;    // 检测到的敏感词集合
    
    public SensitiveCheckResult(boolean containsSensitiveWord, Set<String> sensitiveWords) {
        this.containsSensitiveWord = containsSensitiveWord;
        this.sensitiveWords = Collections.unmodifiableSet(
            sensitiveWords != null ? sensitiveWords : Collections.emptySet()
        );
    }

    //--- Getter 方法 ---//
    public boolean isContainsSensitiveWord() {
        return containsSensitiveWord;
    }

    public Set<String> getSensitiveWords() {
        return sensitiveWords;
    }

    //--- 实用方法 ---//
    /**
     * 获取第一个检测到的敏感词（用于快速提示）
     */
    public Optional<String> getFirstWord() {
        return sensitiveWords.stream().findFirst();
    }

    /**
     * 获取敏感词数量
     */
    public int getWordCount() {
        return sensitiveWords.size();
    }

    @Override
    public String toString() {
        return String.format("检测结果[%s敏感词: %s]", 
            containsSensitiveWord ? "含" : "无", 
            String.join(",", sensitiveWords));
    }
}