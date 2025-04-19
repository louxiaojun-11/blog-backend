package com.lxj.myblog.service;

import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.entity.User;
import com.lxj.myblog.domain.vo.InformationVO;
import com.lxj.myblog.domain.vo.UserProfileVO;
import com.lxj.myblog.result.PageResult;

public interface UserService {
    User login(UserLoginDTO userLoginDTO);


    void update(UserInfoDTO userInfoDTO);

    String getPasswordByUserId(Integer userId);

    UserProfileVO getUserProfile(Integer userId);

    void updateIntroduce(IntroduceDTO introduceDTO);

    void register(RegisterDTO registerDTO);
    PageResult musicPageQuery(MediaListDTO mediaListDTO);

    void uploadMusic(MusicUploadDTO musicUploadDTO);

    void updateUserStatus(Integer senderId, String status);

    PageResult pageQueryInformation(InformationPageDTO informationPageDTO);

    InformationVO getInformationDetail(Integer informationId);



}
