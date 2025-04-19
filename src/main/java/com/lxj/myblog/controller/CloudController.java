package com.lxj.myblog.controller;

import com.lxj.myblog.constant.MessageConstant;
import com.lxj.myblog.domain.dto.MediaListDTO;
import com.lxj.myblog.domain.dto.MultiMediaUploadDTO;
import com.lxj.myblog.domain.dto.MusicUploadDTO;
import com.lxj.myblog.domain.response.ApiResponse;
import com.lxj.myblog.result.PageResult;
import com.lxj.myblog.service.CloudService;
import com.lxj.myblog.service.UserService;
import com.lxj.myblog.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cloud")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class CloudController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @Autowired
    private CloudService cloudService;

    @PostMapping("/uploadMultimedia")
    public ApiResponse uploadMultimedia(MultipartFile file) {
        log.info("上传文件：{}", file);
        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            List<String> allowedMusicExtensions = Arrays.asList(".mp3", ".wav", ".ogg", ".flac");
            List<String> allowedVideoExtensions = Arrays.asList(".mp4", ".avi", ".mov", ".mkv");
            List<String> allowedPhotoExtensions = Arrays.asList(".jpg",".jpeg",".png", ".raw",".webp");
            if (!allowedMusicExtensions.contains(extension.toLowerCase())
                    && !allowedVideoExtensions.contains(extension.toLowerCase())
                    && !allowedPhotoExtensions.contains(extension.toLowerCase())){
                return ApiResponse.error("不支持的文件类型，音乐上传支持格式: " + allowedMusicExtensions
                        +"\n视频上传支持格式: "+allowedVideoExtensions
                        +"\n图片上传支持格式: "+allowedPhotoExtensions);
            }
//            if(file.getSize()>10485760){
//                return ApiResponse.error("文件大小超过限制，最大10MB");
//            }
            String fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            //截取原始文件名的后缀
            String objectName = UUID.randomUUID() + extension;
            String filepath =aliOssUtil.upload(file.getBytes(),objectName);
            MultiMediaUploadDTO multiMediaFile = new MultiMediaUploadDTO();
            multiMediaFile.setFileName(fileName);
            multiMediaFile.setUrl(filepath);
            if(allowedMusicExtensions.contains(extension.toLowerCase())){
                multiMediaFile.setType("music");
            }
            if(allowedVideoExtensions.contains(extension.toLowerCase())){
                multiMediaFile.setType("video");
            }
            if(allowedPhotoExtensions.contains(extension.toLowerCase())){
                multiMediaFile.setType("photo");
            }
            cloudService.uploadMultiMediaFile(multiMediaFile);
            return ApiResponse.success();
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return ApiResponse.error(MessageConstant.UPLOAD_FAILED);
    }

    @GetMapping("/musicList")
    public ApiResponse<PageResult> getMusicList( MediaListDTO mediaListDTO){
        PageResult pageResult = cloudService.musicPageQuery(mediaListDTO);
        return ApiResponse.success(pageResult);
    }
    @GetMapping("/videoList")
    public ApiResponse<PageResult> getVideoList( MediaListDTO mediaListDTO){
        PageResult pageResult = cloudService.videoPageQuery(mediaListDTO);
        return ApiResponse.success(pageResult);
    }
    @GetMapping("/photoList")
    public ApiResponse<PageResult> getPhotoList( MediaListDTO mediaListDTO){
        PageResult pageResult = cloudService.photoPageQuery(mediaListDTO);
        return ApiResponse.success(pageResult);
    }

    @DeleteMapping("/deleteFile/{fileId}")
     public ApiResponse deleteFile(@PathVariable Integer fileId){
        cloudService.deleteFile(fileId);
         return ApiResponse.success();
     }
}
