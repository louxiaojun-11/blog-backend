package com.lxj.myblog.service;

import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.vo.BlogVO;
import com.lxj.myblog.domain.vo.UserBlogVO;
import com.lxj.myblog.result.PageResult;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BlogService {
    PageResult pageQuery(BlogPageQueryDTO blogPageQueryDTO);

    void upload(BlogDTO blogDTO);

    void deleteById(Integer id);

    int findLike(LikeBlogDTO likeBlogDTO);

    void addLike(LikeBlogDTO likeBlogDTO);

    void removeLike(LikeBlogDTO likeBlogDTO);

    PageResult commentPageQuery(CommentPageQueryDTO commentPageQueryDTO);

    void addComment(CommentDTO commentDTO);
    
    List<BlogVO> list();

    PageResult searchBlogByPage(SearchBlogDTO searchBlogDTO);

    void updateTime(Integer userId);


    UserBlogVO getDetail(Integer blogId);
}
