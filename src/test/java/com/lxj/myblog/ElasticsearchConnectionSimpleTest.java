package com.lxj.myblog;

import com.lxj.myblog.esdao.BlogEsDao;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

//@SpringBootest
public class ElasticsearchConnectionSimpleTest implements CommandLineRunner {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private BlogEsDao blogEsDao;
    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchConnectionSimpleTest.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            // 传入 RequestOptions.DEFAULT 作为参数
            boolean isConnected = restHighLevelClient.ping(RequestOptions.DEFAULT);
            if (isConnected) {
                System.out.println("成功连接到 Elasticsearch");
            } else {
                System.out.println("未能连接到 Elasticsearch");
            }
        } catch (Exception e) {
            System.err.println("连接 Elasticsearch 时出现异常: " + e.getMessage());
            e.printStackTrace();
        }
        // 关闭应用程序，因为这只是一个测试
        System.exit(0);
    }


}
