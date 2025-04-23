package com.lxj.myblog.service;

import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.vo.GroupBlogDetailVO;
import com.lxj.myblog.domain.vo.GroupBlogUserVO;
import com.lxj.myblog.domain.vo.GroupInfoVO;
import com.lxj.myblog.result.PageResult;

import java.util.Date;

public interface HobbyService {
    PageResult groupPageQuery(HobbyGroupPageDTO hobbyGroupPageDTO);

    void uploadHobbyBlog(HobbyBlogDTO hobbyBlogDTO);

    void createGroup(GroupCreateDTO groupCreateDTO);

    void joinGroup(JoinGroupDTO joinGroupDTO);


    PageResult groupBlogListPageQuery(GroupBlogListPageDTO groupBlogListPageDTO);

    GroupInfoVO getGroupInfo(Integer groupId);

    Integer isGroupMember(Integer groupId, Integer userId);

    void quitGroup(JoinGroupDTO joinGroupDTO);

    GroupBlogDetailVO getGroupBlogDetail(Integer blogId);

    GroupBlogUserVO getGroupBlogUser(Integer userId);

    int findLike(LikeBlogDTO likeBlogDTO);

    void addLike(LikeBlogDTO likeBlogDTO);

    void removeLike(LikeBlogDTO likeBlogDTO);

    PageResult commentPageQuery(CommentPageQueryDTO commentPageQueryDTO);

    void addComment(CommentDTO commentDTO);

    PageResult userHobbyBlogPageQuery(BlogPageQueryDTO blogPageQueryDTO);


}
