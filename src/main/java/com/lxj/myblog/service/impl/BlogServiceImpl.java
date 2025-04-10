package com.lxj.myblog.service.impl;

import cn.hutool.core.date.DateTime;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxj.myblog.Repository.BlogEsRepository;
import com.lxj.myblog.context.BaseContext;
import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.entity.Blog;
import com.lxj.myblog.domain.entity.Comment;
import com.lxj.myblog.domain.vo.AuthorVO;
import com.lxj.myblog.domain.vo.BlogVO;
import com.lxj.myblog.domain.vo.MusicVO;
import com.lxj.myblog.domain.vo.UserBlogVO;
import com.lxj.myblog.mapper.BlogMapper;
import com.lxj.myblog.mapper.UserMapper;
import com.lxj.myblog.result.PageResult;
import com.lxj.myblog.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.data.elasticsearch.core.SearchHit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageImpl;

@Service
@Slf4j
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    BlogEsRepository blogEsRepository;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private UserMapper userMapper;
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
        //通过id删除es中的数据，因为es中没有分页，所以直接通过id删除即可。
        blogEsRepository.deleteById(String.valueOf(id));

    }

    @Override
    public int findLike(LikeBlogDTO likeBlogDTO) {
        if (blogMapper.findLike(likeBlogDTO) == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void addLike(LikeBlogDTO likeBlogDTO) {
        blogMapper.addLikeAmount(likeBlogDTO);
        blogMapper.addLike(likeBlogDTO);
        Integer blogId = likeBlogDTO.getBlogId();
        Integer operationUserId = likeBlogDTO.getUserId();
        String blogName = blogMapper.getBlogTitle(blogId);
        String username = userMapper.getUsernameById(operationUserId);
        Integer userId = blogMapper.getBlogUserId(blogId);
        String notice = username + " 点赞了你的博文:<" + blogName + ">";
        if(operationUserId != userId) {
            blogMapper.sendNotice(userId, notice, "blogLike", null, operationUserId);
        }
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
        Integer blogId = commentDTO.getBlogId();
        String content = commentDTO.getContent();
        String blogName = blogMapper.getBlogTitle(blogId);
        Integer operationUserId = commentDTO.getUserId();
        String username = userMapper.getUsernameById(operationUserId);
        Integer userId = blogMapper.getBlogUserId(blogId);
        String notice = username + "给你的博文:<" + blogName + "> 评论了:" + content;
        if (operationUserId != userId) {
            blogMapper.sendNotice(userId, notice, "blogComment", null, operationUserId);
        }

    }

    @Override
    public List<BlogVO> list() {
        List<BlogVO> blogList;
        blogList = blogMapper.list();
        blogList.forEach(blogVO -> {
            AuthorVO author = blogMapper.getAuthor(blogVO.getUserId());
            blogVO.setAuthor(author);
        });
        return blogList;
    }

    @Override
    public PageResult searchBlogByPage(SearchBlogDTO searchBlogDTO) {
        // 使用 MultiMatchQueryBuilder 同时在 title 和 content 字段上进行查询
        String keyword = searchBlogDTO.getKeyword();
        Integer page = searchBlogDTO.getPage();
        Integer pageSize = searchBlogDTO.getPageSize();
        // 1. 构建多字段查询
        MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(keyword, "title", "content");

        // 2. 修正页码：page-1 转换为从0开始的索引
        PageRequest pageable = PageRequest.of(page - 1, pageSize);

        // 3. 构造查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQuery)
                .withPageable(pageable)
                .build();

        // 4. 执行查询
        SearchHits<BlogVO> searchHits = elasticsearchRestTemplate.search(searchQuery, BlogVO.class);

        // 5. 提取当前页数据
        List<BlogVO> content = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
        content.forEach(blogVO -> {
            blogVO.setLikes(blogMapper.getLikesAmount(blogVO.getId()));
            blogVO.setComments(blogMapper.getCommentsAmount(blogVO.getId()));
        });

        // 6. 获取总记录数
        long totalHits = searchHits.getTotalHits(); // 或 searchHits.getTotalHits().value（ES 7.0+）

        return new PageResult(totalHits, content);
    }

    @Override
    public void updateTime(Integer userId) {
        DateTime now = DateTime.now();
        blogMapper.updateTime(userId, now);
    }

    @Override
    public UserBlogVO getDetail(Integer blogId) {
        return blogMapper.getDetail(blogId);
    }
}


