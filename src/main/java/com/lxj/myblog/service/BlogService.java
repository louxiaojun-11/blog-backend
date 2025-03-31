package com.lxj.myblog.service;

import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.vo.BlogVO;
import com.lxj.myblog.result.PageResult;

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


}
