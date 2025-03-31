package com.lxj.myblog.controller;

import com.lxj.myblog.context.BaseContext;
import com.lxj.myblog.domain.dto.BlogPageQueryDTO;
import com.lxj.myblog.domain.dto.FriendDTO;
import com.lxj.myblog.domain.response.ApiResponse;
import com.lxj.myblog.domain.vo.RelationVO;
import com.lxj.myblog.domain.vo.RelationProfileVO;
import com.lxj.myblog.service.FriendService;
import com.lxj.myblog.service.RelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/relation")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class RelationController {

    @Autowired
    private FriendService friendService;
    @Autowired
    private RelationService relationService;


    @GetMapping("/friends")
    public ApiResponse<List<FriendDTO>> getFriends() {
        try {
            List<FriendDTO> friends = friendService.getFriendsList(BaseContext.getCurrentId().intValue());
            return ApiResponse.success(friends);
        } catch (Exception e) {
            log.error("Error in getFriends: {}", e.getMessage());
            return ApiResponse.error("Failed to get friends list");
        }
    }

    @GetMapping("/profile")
    public ApiResponse<RelationProfileVO> getRelationProfile(BlogPageQueryDTO blogPageQueryDTO){
        return ApiResponse.success(relationService.getRelationProFile(blogPageQueryDTO));

    }
    @GetMapping("/follower")
    public ApiResponse<List<RelationVO>> getFollowerList(){
        Integer userId = BaseContext.getCurrentId().intValue();
        return ApiResponse.success(relationService.getFollowerList(userId));
    }

    @GetMapping("/following")
    public ApiResponse<List<RelationVO>> getFollowingList(){
        Integer userId = BaseContext.getCurrentId().intValue();
        return ApiResponse.success(relationService.getFollowingList(userId));
    }
    @GetMapping("/search")
    public ApiResponse<List<RelationVO>> getSearchList(String searchContent){
        Integer userId = BaseContext.getCurrentId().intValue();
        List<RelationVO> searchList = relationService.getSearchList(searchContent);
        log.info(searchList.toString());
        return ApiResponse.success(searchList);

    }

    @PutMapping("/follow")
    public ApiResponse followRelation(Integer relationId){
        Integer userId = BaseContext.getCurrentId().intValue();
        relationService.followRelation(userId,relationId);
        return ApiResponse.success();
    }

    @PutMapping("/cancelfollow")
    public ApiResponse cancelFollow(Integer relationId){
        Integer userId = BaseContext.getCurrentId().intValue();
        relationService.cancelFollow(userId,relationId);
        return ApiResponse.success();
    }

}