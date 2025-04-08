package com.lxj.myblog.controller.manage;
import com.lxj.myblog.domain.dto.CommentPageQueryDTO;
import com.lxj.myblog.domain.dto.UserPageQueryDTO;
import com.lxj.myblog.domain.response.ApiResponse;
import com.lxj.myblog.result.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage/user")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class UserManageController{
    @GetMapping("/userList")
    public ApiResponse<PageResult> getUserList(UserPageQueryDTO userPageQueryDTO){
       return null;
    }

}