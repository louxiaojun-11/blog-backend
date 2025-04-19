package com.lxj.myblog.service;

import com.lxj.myblog.domain.dto.MediaListDTO;
import com.lxj.myblog.domain.dto.MultiMediaUploadDTO;
import com.lxj.myblog.result.PageResult;

public interface CloudService {

    PageResult musicPageQuery(MediaListDTO mediaListDTO);
    PageResult videoPageQuery(MediaListDTO mediaListDTO);
    PageResult photoPageQuery(MediaListDTO mediaListDTO);
    void uploadMultiMediaFile(MultiMediaUploadDTO multiMediaFile);

    void deleteFile(Integer fileId);
}
