package com.lxj.myblog.controller;

import com.lxj.myblog.Properties.JwtProperties;
import com.lxj.myblog.constant.JwtClaimsConstant;
import com.lxj.myblog.context.BaseContext;
import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.entity.User;
import com.lxj.myblog.domain.response.ApiResponse;
import com.lxj.myblog.domain.vo.UserLoginVO;
import com.lxj.myblog.domain.vo.UserProfileVO;
import com.lxj.myblog.result.PageResult;
import com.lxj.myblog.service.BlogService;
import com.lxj.myblog.service.UserService;
import com.lxj.myblog.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class UserController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    @GetMapping("/bloglist")
    public ApiResponse<PageResult> getBlog( BlogPageQueryDTO blogPageQueryDTO){
        Integer id = BaseContext.getCurrentId().intValue();
        log.info("博客分页查询，参数为{}", blogPageQueryDTO);
        PageResult pageResult =blogService.pageQuery(blogPageQueryDTO);
        return ApiResponse.success(pageResult);
    }

    @PostMapping("/login")
    public ApiResponse<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        User user = userService.login(userLoginDTO);
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getUserId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .userId(user.getUserId())
                .account(user.getAccount())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .token(token)
                .build();
        log.info("登录成功:{}",userLoginVO);
        return ApiResponse.success(userLoginVO);
    }
    @PutMapping
    public ApiResponse update(@RequestBody UserInfoDTO userInfoDTO,@RequestHeader("Token") String jwtToken) {
        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), jwtToken);
            Integer currentUserId = Integer.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            // 检查请求的用户 ID 与要更新的用户 ID 是否一致
            if (!currentUserId.equals(BaseContext.getCurrentId().intValue())) {
                return ApiResponse.error("您没有权限更新该用户的信息");
            }
            if(userInfoDTO.getPassword() != null && userInfoDTO.getPassword().length()< 8){
                return ApiResponse.error("密码长度不能小于8位");
            }
            log.info("更新用户信息：{}", userInfoDTO);
            userService.update(userInfoDTO);
            blogService.updateTime(userInfoDTO.getUserId());
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("更新用户信息失败: {}", e.getMessage());
            return ApiResponse.error("更新用户信息失败");
        }


    }
    @GetMapping("/password")
    public ApiResponse<String> getPasswordByUserId(Integer userId){
        if(userId != BaseContext.getCurrentId().intValue()){
            return ApiResponse.error("您没有权限修改该用户的信息");
        }
        return ApiResponse.success(userService.getPasswordByUserId(userId));
    }

    @GetMapping("/profile/{userId}")
    public ApiResponse<UserProfileVO> getUserProfile(@PathVariable("userId") Integer userId){
        UserProfileVO userProfile = userService.getUserProfile(userId);
        return  ApiResponse.success(userProfile);
    }

    @PutMapping("profile/introduce")
    public ApiResponse updateIntroduce(@RequestBody IntroduceDTO introduceDTO){
        userService.updateIntroduce(introduceDTO);
        return ApiResponse.success();
    }
    @PostMapping("/register")
    public ApiResponse userRegister(@RequestBody RegisterDTO registerDTO){
        if(registerDTO.getPassword().length() < 8){
            return ApiResponse.error("密码长度不能小于8位");
        }
        userService.register(registerDTO);
        return ApiResponse.success();
    }
    @GetMapping("/musicList")
    public ApiResponse<PageResult> getMusicList( MediaListDTO mediaListDTO){
        PageResult pageResult = userService.musicPageQuery(mediaListDTO);
        return ApiResponse.success(pageResult);
    }

    @PostMapping("/musicUpload")
    public ApiResponse uploadMusic(@RequestBody MusicUploadDTO musicUploadDTO){
        userService.uploadMusic(musicUploadDTO);
        return ApiResponse.success();
    }

    @GetMapping("/informationList")
    public ApiResponse<PageResult> getInformation(InformationPageDTO informationPageDTO){
        PageResult pageResult =userService.pageQueryInformation(informationPageDTO);
        return ApiResponse.success(pageResult);
    }
}
