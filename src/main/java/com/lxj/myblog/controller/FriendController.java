package com.lxj.myblog.controller;

import com.lxj.myblog.context.BaseContext;
import com.lxj.myblog.domain.dto.FriendDTO;
import com.lxj.myblog.domain.response.ApiResponse;
import com.lxj.myblog.service.FriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/friends")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class FriendController {

    @Autowired
    private FriendService friendService;

    @GetMapping
    public ApiResponse<List<FriendDTO>> getFriends() {
        try {
            List<FriendDTO> friends = friendService.getFriendsList(BaseContext.getCurrentId().intValue());
            return ApiResponse.success(friends);
        } catch (Exception e) {
            log.error("Error in getFriends: {}", e.getMessage());
            return ApiResponse.error("Failed to get friends list");
        }
    }

//    @PostMapping("/status")
//    public ApiResponse<Void> updateStatus(@RequestParam String status) {
//        try {
//            // TODO 这里暂时硬编码用户ID，实际应该从认证信息中获取
//            Long currentUserId = 1L;
//            friendService.updateUserStatus(currentUserId, status);
//            return ApiResponse.success(null);
//        } catch (Exception e) {
//            log.error("Error in updateStatus: {}", e.getMessage());
//            return ApiResponse.error("Failed to update status");
//        }
//    }
}


