package com.lxj.myblog.controller;

import com.lxj.myblog.Properties.JwtProperties;
import com.lxj.myblog.constant.JwtClaimsConstant;
import com.lxj.myblog.context.BaseContext;
import com.lxj.myblog.domain.dto.FriendDTO;
import com.lxj.myblog.domain.entity.ChatRecord;
import com.lxj.myblog.domain.response.ApiResponse;
import com.lxj.myblog.service.ChatService;
import com.lxj.myblog.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private ChatService chatService;

    @GetMapping("/record")
    public ApiResponse<List<ChatRecord>> getChatRecord(@RequestHeader("token") String token,Integer receiverId) {
        Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
        Integer senderId =(Integer)claims.get(JwtClaimsConstant.USER_ID);
        return ApiResponse.success(chatService.getChatRecord(senderId,receiverId));
    }

}
