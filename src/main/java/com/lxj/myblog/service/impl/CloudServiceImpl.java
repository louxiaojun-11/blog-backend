package com.lxj.myblog.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lxj.myblog.context.BaseContext;
import com.lxj.myblog.domain.dto.MediaListDTO;
import com.lxj.myblog.domain.dto.MultiMediaUploadDTO;
import com.lxj.myblog.domain.vo.MultiMediaVO;
import com.lxj.myblog.mapper.CloudMapper;
import com.lxj.myblog.result.PageResult;
import com.lxj.myblog.service.CloudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CloudServiceImpl implements CloudService {

    @Autowired
    private CloudMapper cloudMapper;

    @Override
    public PageResult musicPageQuery(MediaListDTO mediaListDTO) {
        mediaListDTO.setUserId(BaseContext.getCurrentId().intValue());
        PageHelper.startPage(mediaListDTO.getPage(), mediaListDTO.getPageSize());
        Page<MultiMediaVO> page = cloudMapper.musicPageQuery(mediaListDTO);
        long total = page.getTotal();
        List<MultiMediaVO> records = page.getResult();
        return new PageResult(total, records);
    }
    @Override
    public PageResult videoPageQuery(MediaListDTO mediaListDTO) {
        mediaListDTO.setUserId(BaseContext.getCurrentId().intValue());
        PageHelper.startPage(mediaListDTO.getPage(), mediaListDTO.getPageSize());
        Page<MultiMediaVO> page = cloudMapper.videoPageQuery(mediaListDTO);
        long total = page.getTotal();
        List<MultiMediaVO> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public PageResult photoPageQuery(MediaListDTO mediaListDTO) {
        mediaListDTO.setUserId(BaseContext.getCurrentId().intValue());
        PageHelper.startPage(mediaListDTO.getPage(), mediaListDTO.getPageSize());
        Page<MultiMediaVO> page = cloudMapper.photoPageQuery(mediaListDTO);
        long total = page.getTotal();
        List<MultiMediaVO> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public void uploadMultiMediaFile(MultiMediaUploadDTO multiMediaFile) {
        multiMediaFile.setUserId(BaseContext.getCurrentId().intValue());
        cloudMapper.uploadMultiMediaFile(multiMediaFile);
    }

    @Override
    public void deleteFile(Integer fileId) {
        cloudMapper.deleteFile(fileId);
    }

}
