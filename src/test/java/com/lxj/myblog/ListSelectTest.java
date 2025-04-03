package com.lxj.myblog;

import com.lxj.myblog.domain.vo.BlogVO;
import com.lxj.myblog.service.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@SpringBootTest
public class ListSelectTest {
    @Autowired
    BlogService blogService;
    @Test
    void print() {
        List<BlogVO> list = blogService.list();
        System.out.println(list);
    }
}
