package com.lxj.myblog.controller;

import com.lxj.myblog.constant.MessageConstant;
import com.lxj.myblog.domain.dto.MultiMediaUploadDTO;
import com.lxj.myblog.domain.dto.MusicUploadDTO;
import com.lxj.myblog.domain.response.ApiResponse;
import com.lxj.myblog.mapper.UserMapper;
import com.lxj.myblog.service.UserService;
import com.lxj.myblog.utils.AliOssUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class UploadController {
    @Autowired
    private AliOssUtil aliOssUtil;
    @Autowired
    private UserService userService;
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
    @PostMapping("/uploadMusic")
    public ApiResponse<MusicUploadDTO> uploadMusic(MultipartFile file) {
        log.info("上传文件：{}", file);
        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            List<String> allowedExtensions = Arrays.asList(".mp3", ".wav", ".ogg", ".flac");
            if (!allowedExtensions.contains(extension.toLowerCase())) {
                return ApiResponse.error("不支持的文件类型，仅支持: " + allowedExtensions);
            }
            if(file.getSize()>10485760){
                return ApiResponse.error("文件大小超过限制，最大10MB");
            }
            String musicName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            //截取原始文件名的后缀
            String objectName = UUID.randomUUID() + extension;
            String filepath =aliOssUtil.upload(file.getBytes(),objectName);
            MusicUploadDTO music = new MusicUploadDTO();
            music.setMusicName(musicName);
            music.setMusicURL(filepath);
            userService.uploadMusic(music);
            return ApiResponse.success(music);
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return ApiResponse.error(MessageConstant.UPLOAD_FAILED);
    }


}