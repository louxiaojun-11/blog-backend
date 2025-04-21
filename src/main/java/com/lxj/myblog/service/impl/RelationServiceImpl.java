package com.lxj.myblog.service.impl;

import com.lxj.myblog.constant.RelationStatusConstant;
import com.lxj.myblog.context.BaseContext;
import com.lxj.myblog.domain.dto.BlogPageQueryDTO;
import com.lxj.myblog.domain.entity.Blog;
import com.lxj.myblog.domain.entity.User;
import com.lxj.myblog.domain.response.ApiResponse;
import com.lxj.myblog.domain.vo.RelationProfileVO;
import com.lxj.myblog.domain.vo.RelationVO;
import com.lxj.myblog.mapper.BlogMapper;
import com.lxj.myblog.mapper.RelationMapper;
import com.lxj.myblog.mapper.UserMapper;
import com.lxj.myblog.service.BlogService;
import com.lxj.myblog.service.RelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RelationServiceImpl implements RelationService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RelationMapper relationMapper;
    @Autowired
    BlogService blogService;
    @Autowired
    BlogMapper blogMapper;

    @Override
    public RelationProfileVO getRelationProFile(BlogPageQueryDTO blogPageQueryDTO) {
        //TODO id为硬编码
        Integer userId = BaseContext.getCurrentId().intValue();
        RelationProfileVO relationProfileVO = new RelationProfileVO();
        //获取relation的基本信息
        relationProfileVO.setRelationId(blogPageQueryDTO.getUserId());
        User user = userMapper.getByUserId(blogPageQueryDTO.getUserId());
        BeanUtils.copyProperties(user, relationProfileVO);
        relationProfileVO.setFollower(userMapper.getFollowerAmount(blogPageQueryDTO.getUserId()));
        relationProfileVO.setFollowing(userMapper.getFollowingAmount(blogPageQueryDTO.getUserId()));
        relationProfileVO.setIntroduce(userMapper.getIntroduce(blogPageQueryDTO.getUserId()));
        //获取relation的博客列表 TODO 下方分页查询功能调试后需要取消注释
        relationProfileVO.setPageResult(blogService.pageQuery(blogPageQueryDTO));
        //查询数据库的关注关系表来判断当前用户和关系用户之间的关注关系
        relationProfileVO.setStatus(getRelationStatus(userId,blogPageQueryDTO.getUserId()));
        return relationProfileVO;
    }

    @Override
    public Integer getRelationStatus(Integer userId, Integer relationId) {
        //1.二者互关
        if (relationMapper.countCurrentFollow(userId, relationId) == 1 && relationMapper.countRelationFollow(userId, relationId) == 1)
            return RelationStatusConstant.MUTUAL_FOLLOWING;

            //2.当前用户单向关注关系用户
        else if (relationMapper.countCurrentFollow(userId, relationId) == 1 && relationMapper.countRelationFollow(userId, relationId) == 0)
           return RelationStatusConstant.CURRENT_FOLLOWING_RELATION;

            //3.关系用户单向关注当前用户
        else if (relationMapper.countCurrentFollow(userId, relationId) == 0 && relationMapper.countRelationFollow(userId,relationId) == 1)
          return RelationStatusConstant.RELATION_FOLLOWING_CURRENT;

            //二者无互关关系
        else
            return RelationStatusConstant.NO_MUTUAL_FOLLOWING;
    }

    @Override
    public List<RelationVO> getFollowerList(Integer userId) {
        return relationMapper.getFollowerList(userId);
    }

    @Override
    public List<RelationVO> getFollowingList(Integer userId) {
        return relationMapper.getFollowingList(userId);
    }

    @Override
    public List<RelationVO> getSearchList(String searchContent) {
        return relationMapper.getSearchList(searchContent);
    }

    @Override
    public void followRelation(Integer userId, Integer relationId) {
        relationMapper.followRelation(userId,relationId);
        String username = userMapper.getUsernameById(userId);
        String notice = username + " 关注了你!";
        blogMapper.sendNotice(relationId, notice, "userFollow", null, userId);
        if(relationMapper.ifFriend(userId,relationId)==1) {
            relationMapper.addFriend(userId, relationId);
        }
    }

    @Override
    public void cancelFollow(Integer userId, Integer relationId) {
        relationMapper.cancelFollow(userId,relationId);
        relationMapper.cancelFriend(userId,relationId);
    }

}
