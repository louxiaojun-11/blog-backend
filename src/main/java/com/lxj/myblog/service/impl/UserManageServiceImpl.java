package com.lxj.myblog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lxj.myblog.Repository.BlogEsRepository;
import com.lxj.myblog.constant.UserViolationConstant;
import com.lxj.myblog.context.BaseContext;
import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.entity.Blog;
import com.lxj.myblog.domain.entity.BlogViolationRecord;
import com.lxj.myblog.domain.entity.R;
import com.lxj.myblog.domain.vo.UnreviewedGroupVO;
import com.lxj.myblog.domain.vo.UserListVO;
import com.lxj.myblog.mapper.AdminMapper;
import com.lxj.myblog.mapper.BlogMapper;
import com.lxj.myblog.mapper.HobbyMapper;
import com.lxj.myblog.mapper.UserMapper;
import com.lxj.myblog.result.PageResult;
import com.lxj.myblog.service.UserManageService;
import com.lxj.myblog.ws.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.Session;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.lxj.myblog.ws.WebSocketServer.getWebSocketServer;

@Service
@Slf4j
public class UserManageServiceImpl implements UserManageService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private HobbyMapper hobbyMapper;

    @Autowired
    BlogEsRepository blogEsRepository;
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
   @Transactional
    @Override
    public void handleBlogViolation(UserBlogViolationDTO userBlogViolationDTO) throws IOException {
        Integer blogId = userBlogViolationDTO.getBlogId();
        Integer userId = blogMapper.getBlogUserId(blogId);
        String title = blogMapper.getBlogTitle(blogId);
        String content = blogMapper.getBlogContent(blogId);
        Integer adminId =BaseContext.getCurrentId().intValue();
        BlogViolationRecord violationRecord = new BlogViolationRecord();
        violationRecord.setAdminId(adminId);
        violationRecord.setTitle(title);
        violationRecord.setContent(content);
        violationRecord.setUserId(userId);
        BeanUtil.copyProperties(userBlogViolationDTO, violationRecord);
        blogMapper.insertViolationRecord(violationRecord);
        blogMapper.deleteById(blogId);
        blogEsRepository.deleteById(String.valueOf(blogId));
        //发送下架通知给用户
       String notice = "您的博客:<"+title+">由于违规已于"+ LocalDate.now()+ "被管理员下架处理 原因：" + userBlogViolationDTO.getReason();
       blogMapper.sendNotice(userId, notice,"system",adminId,null);
//           WebSocketServer webSocketServer =  getWebSocketServer(userId);
//        Session session = webSocketServer.getSession();
//        String msg = JSON.toJSONString(R.createMsg(true, null, notice), SerializerFeature.WriteNullStringAsEmpty);
//        session.getBasicRemote().sendText(msg);
    }

    @Override
    public PageResult getUnreviewedGroupList(PageQueryDTO pageQueryDTO) {
        PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        Page<UnreviewedGroupVO> page = hobbyMapper.getUnreviewedGroupList();
        long total = page.getTotal();
        List<UnreviewedGroupVO> records = page.getResult();
        records.forEach(group -> {
            group.setUsername(userMapper.getUsernameById(group.getUserId()));
        });
        return new PageResult(total, records);
    }

    @Override
    public void agreeGroup(ReviewGroupDTO reviewGroupDTO) {
        hobbyMapper.agreeGroup(reviewGroupDTO);
        Integer adminId =BaseContext.getCurrentId().intValue();
        String notice = "您创建的圈子: <"+reviewGroupDTO.getGroupName()+"> 已经通过审核!";
        blogMapper.sendNotice(reviewGroupDTO.getUserId(), notice,"system",adminId,null);
    }

    @Override
    public void disagreeGroup(ReviewGroupDTO reviewGroupDTO) {
        hobbyMapper.deleteGroup(reviewGroupDTO);
        Integer adminId =BaseContext.getCurrentId().intValue();
        String notice = "您创建的圈子: <"+reviewGroupDTO.getGroupName()+"> 审核失败,请修改后重新提交审核";
        blogMapper.sendNotice(reviewGroupDTO.getUserId(), notice,"system",adminId,null);
    }

    @Override
    public void handleHobbyBlogViolation(UserBlogViolationDTO userBlogViolationDTO) {
        Integer blogId = userBlogViolationDTO.getBlogId();
        Integer adminId =BaseContext.getCurrentId().intValue();
        Integer userId = hobbyMapper.getBlogUserId(blogId);
        String title = hobbyMapper.getBlogTitle(blogId);
        hobbyMapper.deleteById(blogId);
        //发送下架通知给用户
        String notice = "您的兴趣圈文:<"+title+"> 由于违规已于"+ LocalDate.now()+ "被管理员下架处理 原因：" + userBlogViolationDTO.getReason();
        blogMapper.sendNotice(userId, notice,"system",adminId,null);
//           WebSocketServer webSocketServer =  getWebSocketServer(userId);
    }
}
