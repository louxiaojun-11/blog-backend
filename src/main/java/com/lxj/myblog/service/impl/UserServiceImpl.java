package com.lxj.myblog.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lxj.myblog.constant.MessageConstant;
import com.lxj.myblog.context.BaseContext;
import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.entity.User;
import com.lxj.myblog.domain.vo.*;
import com.lxj.myblog.exception.AccountNotFoundException;
import com.lxj.myblog.exception.PasswordErrorException;
import com.lxj.myblog.mapper.UserMapper;
import com.lxj.myblog.result.PageResult;
import com.lxj.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String account = userLoginDTO.getAccount();
        String password = userLoginDTO.getPassword();
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        User user = userMapper.getByAccount(account);
        //处理异常情况
        if (user == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        if(!user.getPassword().equals(md5Password)){
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        return user;
    }

    @Override
    public void update(UserInfoDTO userInfoDTO) {
        if(userInfoDTO.getPassword() != null ){
            String md5Password = DigestUtils.md5DigestAsHex(userInfoDTO.getPassword().getBytes());
            userInfoDTO.setPassword(md5Password);
        }
        userMapper.update(userInfoDTO);
    }

    @Override
    public String getPasswordByUserId(Integer userId) {
       return  userMapper.getPasswordByUserId(userId);
    }

    @Override
    public UserProfileVO getUserProfile(Integer userId) {
        UserProfileVO userProfile = new UserProfileVO();
        userProfile.setFollower(userMapper.getFollowerAmount(userId));
        userProfile.setFollowing(userMapper.getFollowingAmount(userId));
        userProfile.setIntroduce(userMapper.getIntroduce(userId));
        return userProfile;

    }

    @Override
    public void updateIntroduce(IntroduceDTO introduceDTO) {
        userMapper.updateIntroduce(introduceDTO);
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        if(userMapper.getByAccount(registerDTO.getAccount()) != null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_ALREADY_EXIST);
        }
        else {
            String md5Password = DigestUtils.md5DigestAsHex(registerDTO.getPassword().getBytes());
            registerDTO.setPassword(md5Password);
            userMapper.register(registerDTO);
            IntroduceDTO introduce = new IntroduceDTO();
            introduce.setUserId(registerDTO.getUserId());
            introduce.setIntroduce(registerDTO.getIntroduce());
            userMapper.updateIntroduce(introduce);
        }

    }
    @Override
    public PageResult musicPageQuery(MediaListDTO mediaListDTO) {
        mediaListDTO.setUserId(BaseContext.getCurrentId().intValue());
        PageHelper.startPage(mediaListDTO.getPage(), mediaListDTO.getPageSize());
        Page<MusicVO> page = userMapper.musicPageQuery(mediaListDTO);
        long total = page.getTotal();
        List<MusicVO> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public void uploadMusic(MusicUploadDTO musicUploadDTO) {
        Integer userId = BaseContext.getCurrentId().intValue();
        String musicURL = musicUploadDTO.getMusicURL();
        String musicName = musicUploadDTO.getMusicName();
        userMapper.uploadMusic(userId, musicURL, musicName);
    }

    @Override
    public void updateUserStatus(Integer senderId, String status) {
            userMapper.updateUserStatus(senderId, status);
    }

    @Override
    public PageResult pageQueryInformation(InformationPageDTO informationPageDTO) {
        PageHelper.startPage(informationPageDTO.getPage(), informationPageDTO.getPageSize());
        Page<InformationVO> page = userMapper.pageQueryInformation(informationPageDTO);
        long total = page.getTotal();
        List<InformationVO> records = page.getResult();
        return new PageResult(total, records);
    }


    @Override
    public InformationVO getInformationDetail(Integer informationId) {
        userMapper.addInformationViews(informationId);
        return userMapper.getInformationDetail(informationId);
    }

    @Override
    public PageResult pageQueryNotice(NoticePageDTO noticePageDTO) {
        PageHelper.startPage(noticePageDTO.getPage(), noticePageDTO.getPageSize());
        Page<NoticeVO> page = userMapper.pageQueryNotice(noticePageDTO);
        long total = page.getTotal();
        List<NoticeVO> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public void updateUserLastActiveTime(Integer senderId) {
        userMapper.updateUserLastActiveTime(senderId);
    }

    @Override
    public void deleteNotice(Integer noticeId) {
        userMapper.deleteNotice(noticeId);
    }

    @Override
    public void deleteAllNotice(Integer userId) {
        userMapper.deleteAllNotice(userId);
    }

    @Override
    public List<Integer> getFollowingId(Integer userId) {
        return userMapper.getFollowingId(userId);
    }


}
