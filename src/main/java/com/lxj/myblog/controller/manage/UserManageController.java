package com.lxj.myblog.controller.manage;
import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.entity.Blog;
import com.lxj.myblog.domain.response.ApiResponse;
import com.lxj.myblog.domain.vo.BlogVO;
import com.lxj.myblog.domain.vo.UserBlogVO;
import com.lxj.myblog.result.PageResult;
import com.lxj.myblog.service.BlogService;
import com.lxj.myblog.service.UserManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/manage/user")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class UserManageController{
    @Autowired
    private UserManageService userManageService;
    @Autowired
    private BlogService blogService;

    @GetMapping("/userList")
    public ApiResponse<PageResult> getUserList(UserPageQueryDTO userPageQueryDTO){
        PageResult pageResult =userManageService.getUserList(userPageQueryDTO);
       return ApiResponse.success(pageResult);
    }
    @GetMapping("/userBlogList")
    public ApiResponse<PageResult> getUserBlogList(BlogPageQueryDTO blogPageQueryDTO){
        PageResult pageResult =blogService.pageQuery(blogPageQueryDTO);
        return ApiResponse.success(pageResult);
    }

    @GetMapping("/userBlogDetail")
    public ApiResponse<UserBlogVO> getUserBlogDetail(Integer blogId){
        UserBlogVO blog =  blogService.getDetail(blogId);
        return ApiResponse.success(blog);
    }

    /**
     * 用户违规处理
     * @param
     * @return
     */
    @PostMapping("/userViolation")
    public ApiResponse setUserViolation(@RequestBody  UserViolationDTO userViolationDTO){
        userManageService.setUserViolation(userViolationDTO);
        return ApiResponse.success();
    }
    @PostMapping("/userBlogViolation")
    public ApiResponse handleBlogViolation(@RequestBody UserBlogViolationDTO userBlogViolationDTO) throws IOException {
        try{
            userManageService.handleBlogViolation(userBlogViolationDTO);
            return ApiResponse.success();
        }catch (Exception e){
            return ApiResponse.error("处理失败");
        }

    }
}