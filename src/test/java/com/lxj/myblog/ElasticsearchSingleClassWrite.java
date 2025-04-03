package com.lxj.myblog;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ElasticsearchSingleClassWrite {
    public static void main(String[] args) {
        // 创建 Elasticsearch 高级 REST 客户端
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new org.apache.http.HttpHost("localhost", 9200, "http")));

        try {
            // 准备要写入的数据
            Map<String, Object> blogData = new HashMap<>();
            blogData.put("id", 1);
            blogData.put("title", "示例博文标题");
            blogData.put("content", "这是一篇示例博文的内容。");
            blogData.put("userId", 1001);
            blogData.put("likes", 0);
            blogData.put("views", 0);
            blogData.put("comments", 0);
            blogData.put("createdAt", "2024-10-01 12:00:00");

            // 准备 author 字段的数据
            Map<String, Object> authorData = new HashMap<>();
             authorData.put("id", 1);
            authorData.put("avatar", "pig");
            authorData.put("username", "little pig");
            // 若还有其他字段，可继续添加，例如：
            // authorData.put("id", 2001);

            // 将 author 数据添加到博客数据中
            blogData.put("author", authorData);

            // 创建 IndexRequest 对象
            IndexRequest request = new IndexRequest("blog_index");
            request.id("1"); // 设置文档 ID
            request.source(blogData, XContentType.JSON);

            // 执行索引请求
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);

            // 打印响应信息
            System.out.println("索引操作结果: " + response.getResult());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭客户端
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}