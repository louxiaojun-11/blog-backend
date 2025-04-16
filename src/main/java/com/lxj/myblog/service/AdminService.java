package com.lxj.myblog.service;

import com.lxj.myblog.domain.dto.AdminLoginDTO;
import com.lxj.myblog.domain.dto.InformationDTO;
import com.lxj.myblog.domain.entity.Admin;

public interface AdminService {
    Admin login(AdminLoginDTO adminLoginDTO);

    void releaseInformation(InformationDTO informationDTO);

    void deleteInformation(String informationId);
}
