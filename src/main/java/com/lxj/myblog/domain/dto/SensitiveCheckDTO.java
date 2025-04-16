package com.lxj.myblog.domain.dto;

import com.lxj.myblog.domain.response.SensitiveCheckResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class SensitiveCheckDTO {
    private boolean contains;
    private List<String> words;
    private String message;

    // 静态构造方法
    public static SensitiveCheckDTO fromResult(SensitiveCheckResult result) {
        SensitiveCheckDTO dto = new SensitiveCheckDTO();
        dto.setContains(result.isContainsSensitiveWord());
        dto.setWords(new ArrayList<>(result.getSensitiveWords()));
        dto.setMessage(result.isContainsSensitiveWord() ? 
            "发现" + result.getWordCount() + "个敏感词" : "内容安全");
        return dto;
    }

    //--- Getter & Setter ---//
    // 省略常规getter/setter，实际开发中建议使用Lombok
}