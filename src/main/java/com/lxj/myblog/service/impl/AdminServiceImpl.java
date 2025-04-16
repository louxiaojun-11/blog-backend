package com.lxj.myblog.service.impl;

import com.lxj.myblog.constant.MessageConstant;
import com.lxj.myblog.domain.dto.AdminLoginDTO;
import com.lxj.myblog.domain.dto.InformationDTO;
import com.lxj.myblog.domain.entity.Admin;
import com.lxj.myblog.domain.entity.SensitiveWord;
import com.lxj.myblog.domain.entity.User;
import com.lxj.myblog.exception.AccountNotFoundException;
import com.lxj.myblog.exception.PasswordErrorException;
import com.lxj.myblog.mapper.AdminMapper;
import com.lxj.myblog.mapper.InformationMapper;
import com.lxj.myblog.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private InformationMapper informationMapper;
    @Override
    public Admin login(AdminLoginDTO adminLoginDTO) {
        String account = adminLoginDTO.getAccount();
        String password = adminLoginDTO.getPassword();
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        Admin admin = adminMapper.getByAccount(account);
        //处理异常情况
        if (admin == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        if(!admin.getPassword().equals(md5Password)){
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        return admin;
    }

    @Override
    public void releaseInformation(InformationDTO informationDTO) {
        informationMapper.releaseInformation(informationDTO);
    }

    @Override
    public void deleteInformation(String informationId) {
        informationMapper.deleteInformation(informationId);
    }


}
