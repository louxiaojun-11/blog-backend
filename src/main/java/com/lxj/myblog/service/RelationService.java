package com.lxj.myblog.service;

import com.lxj.myblog.domain.dto.BlogPageQueryDTO;
import com.lxj.myblog.domain.response.ApiResponse;
import com.lxj.myblog.domain.vo.RelationProfileVO;
import com.lxj.myblog.domain.vo.RelationVO;

import java.util.List;

public interface RelationService {
   RelationProfileVO getRelationProFile(BlogPageQueryDTO blogPageQueryDTO);
   Integer getRelationStatus(Integer userId,Integer relationId);

   List<RelationVO> getFollowerList(Integer userId);
   List<RelationVO> getFollowingList(Integer userId);

   List<RelationVO> getSearchList(String searchContent);
   void followRelation(Integer userId, Integer relationId);

   void cancelFollow(Integer userId, Integer relationId);
}
