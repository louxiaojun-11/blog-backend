package com.lxj.myblog.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lxj.myblog.context.BaseContext;
import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.entity.Blog;
import com.lxj.myblog.domain.entity.Comment;
import com.lxj.myblog.domain.vo.AuthorVO;
import com.lxj.myblog.domain.vo.BlogVO;
import com.lxj.myblog.domain.vo.MusicVO;
import com.lxj.myblog.mapper.BlogMapper;
import com.lxj.myblog.result.PageResult;
import com.lxj.myblog.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;
//    @Override
//    public List<BlogVO> getBlogListByUserId(Integer userId) {
//        AuthorVO author = new AuthorVO();
//        author = blogMapper.getAuthor(userId);
//        List<BlogVO> blogList = new ArrayList<>();
//        blogList = blogMapper.getBlogListByUserId(userId);
//        for (BlogVO blogVO : blogList) {
//            blogVO.setAuthor(author);
//        }
//        return blogList;
//    }

    @Override
    public PageResult pageQuery(BlogPageQueryDTO blogPageQueryDTO) {
        PageHelper.startPage(blogPageQueryDTO.getPage(), blogPageQueryDTO.getPageSize());
        Page<Blog> page = blogMapper.pageQuery(blogPageQueryDTO);
        long total = page.getTotal();
        List<Blog> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public void upload(BlogDTO blogDTO) {
        Blog blog = new Blog();
        BeanUtils.copyProperties(blogDTO, blog);
        //TODO 这里需要改成用户登录后获取的用户id
        blog.setUserId(BaseContext.getCurrentId().intValue());
        blog.setComments(0);
        blog.setLikes(0);
        blog.setViews(0);
        blog.setCreatedAt(LocalDateTime.now());
        blog.setUpdatedAt(LocalDateTime.now());
        blogMapper.insert(blog);
    }

    @Override
    public void deleteById(Integer id) {
        blogMapper.deleteById(id);
    }

    @Override
    public int findLike(LikeBlogDTO likeBlogDTO) {
        if (blogMapper.findLike(likeBlogDTO)==1) {
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public void addLike(LikeBlogDTO likeBlogDTO) {
        blogMapper.addLikeAmount(likeBlogDTO);
        blogMapper.addLike(likeBlogDTO);
    }

    @Override
    public void removeLike(LikeBlogDTO likeBlogDTO) {
        blogMapper.removeLikeAmount(likeBlogDTO);
        blogMapper.removeLike(likeBlogDTO);
    }

    @Override
    public PageResult commentPageQuery(CommentPageQueryDTO commentPageQueryDTO) {
        PageHelper.startPage(commentPageQueryDTO.getPage(), commentPageQueryDTO.getPageSize());
        Page<Comment> page = blogMapper.commentPageQuery(commentPageQueryDTO);
        long total = page.getTotal();
        List<Comment> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public void addComment(CommentDTO commentDTO) {
        blogMapper.addCommentAmount(commentDTO);
        blogMapper.addComment(commentDTO);
    }


}
