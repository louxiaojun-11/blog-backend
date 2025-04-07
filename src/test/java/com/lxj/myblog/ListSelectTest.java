package com.lxj.myblog;

import com.lxj.myblog.domain.entity.ChatRecord;
import com.lxj.myblog.domain.vo.BlogVO;
import com.lxj.myblog.mapper.ChatMapper;
import com.lxj.myblog.service.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
@SpringBootTest
public class ListSelectTest {
    @Autowired
    BlogService blogService;
    @Autowired
    private ChatMapper chatMapper
            ;
    @Test
    void save() {
      ChatRecord chatRecord = new ChatRecord();
      chatRecord.setSenderId(1);
      chatRecord.setReceiverId(2);
      chatRecord.setMessage("hello");
      chatRecord.setSendTime(LocalDateTime.now());
      chatMapper.insertChatRecord(chatRecord);
    }
}
