package com.lxj.myblog.domain.dto;

import com.lxj.myblog.domain.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class FriendDTO implements Serializable {
    private Integer userId;
    private String username;
    private String avatar;
    private String status;
    private String lastActive;
    
    public static FriendDTO fromUser(User user) {
        FriendDTO dto = new FriendDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setAvatar(user.getAvatar());
        dto.setStatus(user.getStatus().toString());
        // 格式化最后活跃时间
        dto.setLastActive(formatLastActive(user.getLastActive()));
        return dto;
    }
    
    private static String formatLastActive(LocalDateTime lastActive) {
        if (lastActive == null) return "从未在线";
        Duration duration = Duration.between(lastActive, LocalDateTime.now());
        if (duration.toMinutes() < 5) return "刚刚";
        if (duration.toHours() < 1) return duration.toMinutes() + "分钟前";
        if (duration.toDays() < 1) return duration.toHours() + "小时前";
        return duration.toDays() + "天前";
    }
}