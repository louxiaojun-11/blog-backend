package com.lxj.myblog.service.impl;

import com.lxj.myblog.constant.MessageConstant;
import com.lxj.myblog.domain.dto.AdminLoginDTO;
import com.lxj.myblog.domain.dto.AnnouncementDTO;
import com.lxj.myblog.domain.dto.InformationDTO;
import com.lxj.myblog.domain.entity.Admin;
import com.lxj.myblog.domain.entity.SensitiveWord;
import com.lxj.myblog.domain.entity.User;
import com.lxj.myblog.domain.vo.SystemInfoVO;
import com.lxj.myblog.exception.AccountNotFoundException;
import com.lxj.myblog.exception.PasswordErrorException;
import com.lxj.myblog.mapper.AdminMapper;
import com.lxj.myblog.mapper.InformationMapper;
import com.lxj.myblog.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

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

    @Override
    public void postAnnouncement(AnnouncementDTO announcementDTO) {
        if (announcementDTO.getTarget().equals("all")) {
            List<Integer> userIds = adminMapper.getAllUserIds();
            userIds.forEach(userId -> {
                adminMapper.sendNotice(userId, announcementDTO.getContent(), "system",
                        announcementDTO.getAdminId(), null);
            });
        }
        else if (announcementDTO.getTarget().equals("online")) {
            List<Integer> onlineUserIds = adminMapper.getOnlineUserIds();
            onlineUserIds.forEach(userId -> {
                adminMapper.sendNotice(userId, announcementDTO.getContent(), "system",
                        announcementDTO.getAdminId(), null);
            });
        }
        else{

            Integer userId = Integer.valueOf(announcementDTO.getTarget());
            adminMapper.sendNotice(userId, announcementDTO.getContent(), "system",
                    announcementDTO.getAdminId(), null);
        }
        }

    @Override
    public SystemInfoVO getSystemInfo() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        // 转换为 Date（如果数据库是 timestamp 类型）
        Date startDate = java.sql.Timestamp.valueOf(startOfDay);
        Date endDate = java.sql.Timestamp.valueOf(endOfDay);
        SystemInfoVO info = new SystemInfoVO();
        info.setTodayBlogAmount(adminMapper.countTodayBlogAmount(startDate, endDate));
        info.setTodayActiveUserAmount(adminMapper.countTodayUser(startDate, endDate));
        info.setOnlineUserAmount(adminMapper.countOnlineUser());
        info.setUserAmount(adminMapper.countAllUsers());
        return info;
    }
}



