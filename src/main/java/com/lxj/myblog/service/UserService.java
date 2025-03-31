package com.lxj.myblog.service;

import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.entity.User;
import com.lxj.myblog.domain.vo.UserProfileVO;
import com.lxj.myblog.result.PageResult;

public interface UserService {
    User login(UserLoginDTO userLoginDTO);


    void update(UserInfoDTO userInfoDTO);dd

    String getPasswordByUserId(Integer userId);

    UserProfileVO getUserProfile(Integer userId);

    void updateIntroduce(IntroduceDTO introduceDTO);

    void register(RegisterDTO registerDTO);
    PageResult musicPageQuery(MusicListDTO musicListDTO);

    void uploadMusic(MusicUploadDTO musicUploadDTO);
}
