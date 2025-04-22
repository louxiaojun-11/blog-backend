package com.lxj.myblog.service;

import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.result.PageResult;

import java.io.IOException;

public interface UserManageService {
    PageResult getUserList(UserPageQueryDTO userPageQueryDTO);

    void setUserViolation(UserViolationDTO userViolationDTO);

    void handleBlogViolation(UserBlogViolationDTO userBlogViolationDTO) throws IOException;

    PageResult getUnreviewedGroupList(PageQueryDTO pageQueryDTO);

    void agreeGroup(ReviewGroupDTO reviewGroupDTO);

    void disagreeGroup(ReviewGroupDTO reviewGroupDTO);

    void handleHobbyBlogViolation(UserBlogViolationDTO userBlogViolationDTO);
}
