package com.lxj.myblog.controller;

import com.lxj.myblog.Properties.JwtProperties;
import com.lxj.myblog.constant.JwtClaimsConstant;
import com.lxj.myblog.context.BaseContext;
import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.entity.User;
import com.lxj.myblog.domain.response.ApiResponse;
import com.lxj.myblog.domain.vo.BlogVO;
import com.lxj.myblog.domain.vo.UserLoginVO;
import com.lxj.myblog.domain.vo.UserProfileVO;
import com.lxj.myblog.mapper.UserMapper;
import com.lxj.myblog.result.PageResult;
import com.lxj.myblog.service.BlogService;
import com.lxj.myblog.service.UserService;
import com.lxj.myblog.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
    public ApiResponse update(@RequestBody UserInfoDTO userInfoDTO) {
        log.info("更新用户信息：{}", userInfoDTO);
        userService.update(userInfoDTO);
        return ApiResponse.success();

    }
    @GetMapping("/password")
    public ApiResponse<String> getPasswordByUserId(Integer userId){
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
        userService.register(registerDTO);
        return ApiResponse.success();
    }
    @GetMapping("/musicList")
    public ApiResponse<PageResult> getBlog( MusicListDTO musicListDTO){
        PageResult pageResult = userService.musicPageQuery(musicListDTO);
        return ApiResponse.success(pageResult);
    }
    @PostMapping("/musicUpload")
    public ApiResponse uploadMusic(@RequestBody MusicUploadDTO musicUploadDTO){
        userService.uploadMusic(musicUploadDTO);
        return ApiResponse.success();
    }
}
