package com.lxj.myblog.config;
import com.lxj.myblog.Properties.AliOssProperties;
import com.lxj.myblog.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@Slf4j
public class OssConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssproperties) {
        log.info("开始创建阿里云文件：{}",aliOssproperties);
        //通过传递进来的参数创建阿里云工具类对象
        return new AliOssUtil(aliOssproperties.getEndpoint(),
             aliOssproperties.getAccessKeyId(),
             aliOssproperties.getAccessKeySecret(),
             aliOssproperties.getBucketName());
    }
}
