package com.lxj.myblog.controller;
import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.response.ApiResponse;
import com.lxj.myblog.domain.response.SensitiveCheckResult;
import com.lxj.myblog.domain.vo.GroupBlogDetailVO;
import com.lxj.myblog.domain.vo.GroupBlogUserVO;
import com.lxj.myblog.domain.vo.GroupInfoVO;
import com.lxj.myblog.result.PageResult;
import com.lxj.myblog.service.HobbyService;
import com.lxj.myblog.service.SensitiveWordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/hobby")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class HobbyController {
    @Autowired
    private HobbyService hobbyService;
    @Autowired
    private SensitiveWordService sensitiveWordService;
    @GetMapping("/groupList")
    public ApiResponse<PageResult> getGroupList(HobbyGroupPageDTO hobbyGroupPageDTO) {
        PageResult pageResult = hobbyService.groupPageQuery(hobbyGroupPageDTO);
        return ApiResponse.success(pageResult);
    }
    @PostMapping("/postHobbyBlog")
    public ApiResponse postHobbyBlog(@RequestBody HobbyBlogDTO hobbyBlogDTO) {
        SensitiveCheckResult contentResult = sensitiveWordService.checkText(hobbyBlogDTO.getContent());
        SensitiveCheckResult titleResult = sensitiveWordService.checkText(hobbyBlogDTO.getTitle());
        if(contentResult.isContainsSensitiveWord() || titleResult.isContainsSensitiveWord())
        {
            Set<String> wordsSet = new HashSet<>();
            contentResult.getSensitiveWords().forEach(wordsSet::add);
            titleResult.getSensitiveWords().forEach(wordsSet::add);
            String msg = String.join(",", wordsSet) ;
            return ApiResponse.error("博文包含敏感词: " + msg);
        }
        hobbyService.uploadHobbyBlog(hobbyBlogDTO);
        return ApiResponse.success();
    }
    @PostMapping("/createGroup")
    public ApiResponse createGroup(@RequestBody GroupCreateDTO groupCreateDTO) {
        hobbyService.createGroup(groupCreateDTO);
        return ApiResponse.success();
    }
    @PostMapping("/joinGroup")
    public ApiResponse joinGroup(@RequestBody JoinGroupDTO joinGroupDTO) {
        hobbyService.joinGroup(joinGroupDTO);
        return ApiResponse.success();
    }
    @GetMapping("/groupBlogList")
    public ApiResponse<PageResult> getGroupBlogList(GroupBlogListPageDTO groupBlogListPageDTO ) {
        PageResult pageResult = hobbyService.groupBlogListPageQuery(groupBlogListPageDTO);
        return ApiResponse.success(pageResult);
    }
    @GetMapping("/groupInfo")
    public ApiResponse<GroupInfoVO> getGroupInfo(Integer groupId,Integer userId) {
        GroupInfoVO groupInfoVO = hobbyService.getGroupInfo(groupId);
        groupInfoVO.setStatus(hobbyService.isGroupMember(groupId,userId));
        return ApiResponse.success(groupInfoVO);
    }
    @PutMapping("/quitGroup")
    public ApiResponse<GroupInfoVO> quitGroup(@RequestBody JoinGroupDTO joinGroupDTO) {
        hobbyService.quitGroup(joinGroupDTO);
        return ApiResponse.success();
    }
    @GetMapping("/groupBlogDetail")
    public ApiResponse<GroupBlogDetailVO> getGroupBlogDetail(Integer blogId) {
        return ApiResponse.success(hobbyService.getGroupBlogDetail(blogId));
    }
    @GetMapping("/groupBlogUser")
    public ApiResponse<GroupBlogUserVO> getGroupBlogUser(Integer userId) {
        return ApiResponse.success(hobbyService.getGroupBlogUser(userId));
    }
    @PostMapping("/clickLike")
    public ApiResponse clickLike(@RequestBody LikeBlogDTO likeBlogDTO) {
        if (hobbyService.findLike(likeBlogDTO) == 1) {
            return ApiResponse.success(1);
        } else
            hobbyService.addLike(likeBlogDTO);
        return ApiResponse.success(0);
    }
    @PutMapping("/removeLike")
    public ApiResponse removeLike(@RequestBody LikeBlogDTO likeBlogDTO) {
        hobbyService.removeLike(likeBlogDTO);
        return ApiResponse.success();
    }

    @GetMapping("/commentList")
    public ApiResponse<PageResult> getComment(CommentPageQueryDTO commentPageQueryDTO){
        PageResult pageResult =hobbyService.commentPageQuery(commentPageQueryDTO);
        return ApiResponse.success(pageResult);
    }
    @PostMapping("/addComment")
    public ApiResponse addComment(@RequestBody CommentDTO commentDTO) {
        hobbyService.addComment(commentDTO);
        return ApiResponse.success();
    }
    @GetMapping("/userHobbyBlogList")
    public ApiResponse<PageResult> getUserHobbyBlog(BlogPageQueryDTO blogPageQueryDTO) {
        PageResult pageResult = hobbyService.userHobbyBlogPageQuery(blogPageQueryDTO);
        return ApiResponse.success(pageResult);
    }
}
