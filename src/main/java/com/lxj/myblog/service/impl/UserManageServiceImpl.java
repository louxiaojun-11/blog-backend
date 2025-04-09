package com.lxj.myblog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lxj.myblog.constant.UserViolationConstant;
import com.lxj.myblog.context.BaseContext;
import com.lxj.myblog.domain.dto.UserBlogViolationDTO;
import com.lxj.myblog.domain.dto.UserPageQueryDTO;
import com.lxj.myblog.domain.dto.UserViolationDTO;
import com.lxj.myblog.domain.entity.BlogViolationRecord;
import com.lxj.myblog.domain.vo.UserListVO;
import com.lxj.myblog.mapper.BlogMapper;
import com.lxj.myblog.mapper.UserMapper;
import com.lxj.myblog.result.PageResult;
import com.lxj.myblog.service.UserManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserManageServiceImpl implements UserManageService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BlogMapper blogMapper;
    @Override
    public PageResult getUserList(UserPageQueryDTO userPageQueryDTO) {
        PageHelper.startPage(userPageQueryDTO.getPage(), userPageQueryDTO.getPageSize());
        Page<UserListVO> page = userMapper.getUserList(userPageQueryDTO);
        long total = page.getTotal();
        List<UserListVO> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public void setUserViolation(UserViolationDTO userViolationDTO) {
        Integer userId = userViolationDTO.getUserId();
        Boolean avatarLegal = userViolationDTO.getAvatarLegal();
        Boolean usernameLegal = userViolationDTO.getUsernameLegal();
        if (!avatarLegal) {
            String defaultAvatar = UserViolationConstant.USER_DEFAULT_AVATAR;
            userMapper.updateUserAvatar(userId, defaultAvatar);
        }
        if (!usernameLegal) {
            // 创建 Snowflake 实例
            Snowflake snowflake = IdUtil.createSnowflake(1, 1);
            // 生成雪花算法 ID
            String id = String.valueOf(snowflake.nextId()).substring(8, 14);
            // 生成目标字符串
            String result = "用户" + id;
            userMapper.updateUsername(userId, result);
        }
    }

    @Override
    public void handleBlogViolation(UserBlogViolationDTO userBlogViolationDTO) {
        Integer blogId = userBlogViolationDTO.getBlogId();
        Integer userId = blogMapper.getBlogUserId(blogId);
        BlogViolationRecord violationRecord = new BlogViolationRecord();
        violationRecord.setAdminId(BaseContext.getCurrentId().intValue());
        violationRecord.setTitle(blogMapper.getBlogTitle(blogId));
        violationRecord.setContent(blogMapper.getBlogContent(blogId));
        violationRecord.setUserId(userId);
        BeanUtil.copyProperties(userBlogViolationDTO, violationRecord);
        blogMapper.insertViolationRecord(violationRecord);
        blogMapper.deleteById(blogId);
    }
}
