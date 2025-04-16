package com.lxj.myblog.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.entity.Comment;
import com.lxj.myblog.domain.vo.*;
import com.lxj.myblog.mapper.HobbyMapper;
import com.lxj.myblog.mapper.UserMapper;
import com.lxj.myblog.result.PageResult;
import com.lxj.myblog.service.HobbyService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.select.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class HobbyServiceImpl implements HobbyService {
    @Autowired
    private HobbyMapper hobbyMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public PageResult groupPageQuery(HobbyGroupPageDTO hobbyGroupPageDTO) {
        PageHelper.startPage(hobbyGroupPageDTO.getPage(), hobbyGroupPageDTO.getPageSize());
        Page<HobbyGroupVO> page = hobbyMapper.groupPageQuery(hobbyGroupPageDTO);
        long total = page.getTotal();
        List<HobbyGroupVO> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public void uploadHobbyBlog(HobbyBlogDTO hobbyBlogDTO) {
         hobbyMapper.uploadHobbyBlog(hobbyBlogDTO);
    }

    @Override
    public void createGroup(GroupCreateDTO groupCreateDTO) {
        hobbyMapper.createGroup(groupCreateDTO);
        JoinGroupDTO joinGroupDTO = new JoinGroupDTO();
        joinGroupDTO.setGroupId(groupCreateDTO.getGroupId());
        joinGroupDTO.setUserId(groupCreateDTO.getUserId());
        hobbyMapper.joinGroup(joinGroupDTO);
        hobbyMapper.addGroupMember(joinGroupDTO);
    }

    @Override
    public void joinGroup(JoinGroupDTO joinGroupDTO) {
        hobbyMapper.joinGroup(joinGroupDTO);
        hobbyMapper.addGroupMember(joinGroupDTO);
    }

    @Override
    public PageResult groupBlogListPageQuery(GroupBlogListPageDTO groupBlogListPageDTO) {
        PageHelper.startPage(groupBlogListPageDTO.getPage(), groupBlogListPageDTO.getPageSize());
        Page<GroupBlogListVO> page = hobbyMapper.groupBlogListPageQuery(groupBlogListPageDTO);
        long total = page.getTotal();
        List<GroupBlogListVO> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public GroupInfoVO getGroupInfo(Integer groupId) {
        return hobbyMapper.getGroupInfo(groupId);
    }

    @Override
    public Integer isGroupMember(Integer groupId, Integer userId) {
        return hobbyMapper.isGroupMember(groupId, userId);
    }

    @Override
    public void quitGroup(JoinGroupDTO joinGroupDTO) {
        hobbyMapper.quitGroup(joinGroupDTO);
        hobbyMapper.reduceGroupMember(joinGroupDTO);

    }

    @Override
    public GroupBlogDetailVO getGroupBlogDetail(Integer blogId) {
        return hobbyMapper.getGroupBlogDetail(blogId);
    }

    @Override
    public GroupBlogUserVO getGroupBlogUser(Integer userId) {
        GroupBlogUserVO user = hobbyMapper.getGroupBlogUser(userId);
        user.setIntroduce(userMapper.getIntroduce(userId));
        return user;
    }

    @Override
    public int findLike(LikeBlogDTO likeBlogDTO) {
        if (hobbyMapper.findLike(likeBlogDTO)==1) {
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public void addLike(LikeBlogDTO likeBlogDTO) {
        hobbyMapper.addLikeAmount(likeBlogDTO);
        hobbyMapper.addLike(likeBlogDTO);
    }

    @Override
    public void removeLike(LikeBlogDTO likeBlogDTO) {
        hobbyMapper.removeLikeAmount(likeBlogDTO);
        hobbyMapper.removeLike(likeBlogDTO);
    }

    @Override
    public PageResult commentPageQuery(CommentPageQueryDTO commentPageQueryDTO) {
        PageHelper.startPage(commentPageQueryDTO.getPage(), commentPageQueryDTO.getPageSize());
        Page<Comment> page = hobbyMapper.commentPageQuery(commentPageQueryDTO);
        long total = page.getTotal();
        List<Comment> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public void addComment(CommentDTO commentDTO) {
        hobbyMapper.addCommentAmount(commentDTO);
        hobbyMapper.addComment(commentDTO);
    }

    @Override
    public PageResult userHobbyBlogPageQuery(BlogPageQueryDTO blogPageQueryDTO) {
        PageHelper.startPage(blogPageQueryDTO.getPage(), blogPageQueryDTO.getPageSize());
        Page<UserHobbyBlogVO> page = hobbyMapper.userHobbyBlogPageQuery(blogPageQueryDTO);
        long total = page.getTotal();
        List<UserHobbyBlogVO> records = page.getResult();
        return new PageResult(total, records);
    }


}
