package com.lxj.myblog.service;

import com.lxj.myblog.domain.dto.AdminLoginDTO;
import com.lxj.myblog.domain.dto.AnnouncementDTO;
import com.lxj.myblog.domain.dto.InformationDTO;
import com.lxj.myblog.domain.entity.Admin;
import com.lxj.myblog.domain.entity.SensitiveWord;
import com.lxj.myblog.domain.vo.SystemInfoVO;

public interface AdminService {
    Admin login(AdminLoginDTO adminLoginDTO);

    void releaseInformation(InformationDTO informationDTO);

    void deleteInformation(String informationId);


    void postAnnouncement(AnnouncementDTO announcementDTO);

    SystemInfoVO getSystemInfo();
}
