package com.lxj.myblog.service.impl;

import com.lxj.myblog.domain.entity.ChatRecord;
import com.lxj.myblog.mapper.ChatMapper;
import com.lxj.myblog.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatMapper chatMapper;
    @Override
    public List<ChatRecord> getChatRecord(Integer senderId, Integer receiverId) {
        return chatMapper.getChatRecordsBySenderAndReceiver(senderId, receiverId);
    }
}
