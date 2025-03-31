package com.lxj.myblog.service.impl;

import com.lxj.myblog.domain.dto.FriendDTO;
import com.lxj.myblog.domain.entity.User;
import com.lxj.myblog.mapper.UserMapper;
import com.lxj.myblog.service.FriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FriendServiceImpl implements FriendService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<FriendDTO> getFriendsList(Integer userId) {
        try {
            List<User> friends = userMapper.findFriendsByUserId(userId);
            return friends.stream()
                    .map(FriendDTO::fromUser)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting friends list for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to get friends list");
        }
    }

//    @Override
//    public void updateUserStatus(Long userId, String status) {
//        try {
//            userMapper.updateUserStatus(userId,
//                    status,
//                    LocalDateTime.now());
//        } catch (Exception e) {
//            log.error("Error updating user status: {}", e.getMessage());
//            throw new RuntimeException("Failed to update user status");
//        }
//    }
}