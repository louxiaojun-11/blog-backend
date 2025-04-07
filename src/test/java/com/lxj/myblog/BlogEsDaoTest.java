package com.lxj.myblog;

import com.lxj.myblog.esdao.BlogEsDao;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class BlogEsDaoTest {
    @Resource
    private BlogEsDao blogEsDao;

    @Test
    void findByUserId() {
        System.out.println(blogEsDao.findByUserId(1));
    }
}

