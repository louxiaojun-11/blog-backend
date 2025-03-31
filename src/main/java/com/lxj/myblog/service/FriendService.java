package com.lxj.myblog.service;

import com.lxj.myblog.domain.dto.FriendDTO;

import java.util.List;

public interface FriendService {
    List<FriendDTO> getFriendsList(Integer userId);
//    void updateUserStatus(Long userId, String status);
}