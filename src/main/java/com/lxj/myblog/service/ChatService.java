package com.lxj.myblog.service;

import com.lxj.myblog.domain.entity.ChatRecord;

import java.util.List;

public interface ChatService {
    List<ChatRecord> getChatRecord(Integer senderId, Integer receiverId);
}
