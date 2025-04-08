package com.lxj.myblog.controller.manage;

import com.lxj.myblog.Properties.JwtProperties;
import com.lxj.myblog.constant.JwtClaimsConstant;
import com.lxj.myblog.domain.dto.AdminLoginDTO;
import com.lxj.myblog.domain.dto.UserLoginDTO;
import com.lxj.myblog.domain.entity.Admin;
import com.lxj.myblog.domain.entity.User;
import com.lxj.myblog.domain.response.ApiResponse;
import com.lxj.myblog.domain.vo.AdminLoginVO;
import com.lxj.myblog.domain.vo.UserLoginVO;
import com.lxj.myblog.service.AdminService;
import com.lxj.myblog.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/manage/admin")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class AdminController {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ApiResponse<AdminLoginVO> login(@RequestBody AdminLoginDTO adminLoginDTO){
        Admin admin = adminService.login(adminLoginDTO);
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.ADMIN_ID, admin.getAdminId());
        claims.put(JwtClaimsConstant.STATUS, admin.getStatus());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);
        AdminLoginVO adminLoginVO = AdminLoginVO.builder()
                .adminId(admin.getAdminId())
                .account(admin.getAccount())
                .adminName(admin.getAdminName())
                .avatar(admin.getAvatar())
                .token(token)
                .build();
        Claims claim = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
        String status = claim.get(JwtClaimsConstant.STATUS).toString();
        log.info("登录成功:{},状态:{}",adminLoginVO,status);
        return ApiResponse.success(adminLoginVO);
    }

}