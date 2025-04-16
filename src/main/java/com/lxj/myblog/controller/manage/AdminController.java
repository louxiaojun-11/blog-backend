package com.lxj.myblog.controller.manage;

import com.lxj.myblog.Properties.JwtProperties;
import com.lxj.myblog.constant.JwtClaimsConstant;
import com.lxj.myblog.constant.MessageConstant;
import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.entity.Admin;
import com.lxj.myblog.domain.entity.SensitiveWord;
import com.lxj.myblog.domain.entity.User;
import com.lxj.myblog.domain.response.ApiResponse;
import com.lxj.myblog.domain.vo.AdminLoginVO;
import com.lxj.myblog.domain.vo.InformationVO;
import com.lxj.myblog.domain.vo.UserLoginVO;
import com.lxj.myblog.result.PageResult;
import com.lxj.myblog.service.AdminService;
import com.lxj.myblog.service.SensitiveWordService;
import com.lxj.myblog.service.UserService;
import com.lxj.myblog.utils.AliOssUtil;
import com.lxj.myblog.utils.JwtUtil;
import com.lxj.myblog.ws.WebSocketServer;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.lxj.myblog.ws.WebSocketServer.getWebSocketServer;

@RestController
@RequestMapping("/manage/admin")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class AdminController {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private AliOssUtil aliOssUtil;
    @Autowired
    private SensitiveWordService sensitiveWordService;
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
    @PostMapping("/notice")
    public ApiResponse sendNotice(Integer userId) throws IOException {
        WebSocketServer webSocketServer =  getWebSocketServer(userId);
        Session session = webSocketServer.getSession();
        session.getBasicRemote().sendText("你好");
        return ApiResponse.success();
    }
    @PostMapping("/releaseInformation")
    public ApiResponse releaseInformation(@RequestBody  InformationDTO informationDTO)   {
        adminService.releaseInformation(informationDTO);
        return ApiResponse.success();
    }
    @DeleteMapping("/deleteInformation/{informationId}")
    public ApiResponse deleteInformation(@PathVariable String informationId)   {
        adminService.deleteInformation(informationId);
        return ApiResponse.success();
    }
    @GetMapping("/informationList")
    public ApiResponse<PageResult> getInformation(InformationPageDTO informationPageDTO){
        PageResult pageResult =userService.pageQueryInformation(informationPageDTO);
        return ApiResponse.success(pageResult);
    }
    @GetMapping("/informationDetail")
    public ApiResponse<InformationVO> getInformationDetail(String informationId){
        return ApiResponse.success(userService.getInformationDetail(informationId));
    }
    @PostMapping("/upload")
    public ApiResponse<String> upload(MultipartFile file) {
        log.info("上传文件：{}", file);
        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = UUID.randomUUID() + extension;

            String filepath =aliOssUtil.upload(file.getBytes(),objectName);
            return ApiResponse.success(filepath);
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return ApiResponse.error(MessageConstant.UPLOAD_FAILED);
    }
    @GetMapping("/listWords")
    public ApiResponse<PageResult>listSensitiveWords(SensitiveWordDTO sensitiveWordDTO){
        PageResult pageResult =sensitiveWordService.pageQuerySensitiveWords(sensitiveWordDTO);
        return ApiResponse.success(pageResult);
    }
    @PostMapping("/addWord")
    public ApiResponse addSensitiveWord(@RequestBody WordDTO wordDTO){
        String word = wordDTO.getWord();
        try {
            sensitiveWordService.addWord(word);
        }catch (Exception e){
            return ApiResponse.error("数据库中已有该敏感词");
        }
        return ApiResponse.success();
    }
    @DeleteMapping("/deleteWord/{id}")
    public ApiResponse addSensitiveWord(@PathVariable Integer id){
        sensitiveWordService.deleteWord(id);
        return ApiResponse.success();
    }
}