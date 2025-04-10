package com.lxj.myblog.service;

import com.lxj.myblog.domain.dto.UserBlogViolationDTO;
import com.lxj.myblog.domain.dto.UserPageQueryDTO;
import com.lxj.myblog.domain.dto.UserViolationDTO;
import com.lxj.myblog.result.PageResult;

import java.io.IOException;

public interface UserManageService {
    PageResult getUserList(UserPageQueryDTO userPageQueryDTO);

    void setUserViolation(UserViolationDTO userViolationDTO);

    void handleBlogViolation(UserBlogViolationDTO userBlogViolationDTO) throws IOException;
}
