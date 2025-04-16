package com.lxj.myblog.controller;


import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.response.ApiResponse;
import com.lxj.myblog.domain.response.SensitiveCheckResult;
import com.lxj.myblog.domain.vo.BlogVO;
import com.lxj.myblog.result.PageResult;
import com.lxj.myblog.service.BlogService;
import com.lxj.myblog.service.SensitiveWordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/blog")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private SensitiveWordService sensitiveWordService;
    @PostMapping
    public ApiResponse uploadBlog(@RequestBody BlogDTO blogDTO) {
        //检测敏感词
        SensitiveCheckResult contentResult = sensitiveWordService.checkText(blogDTO.getContent());
        SensitiveCheckResult titleResult = sensitiveWordService.checkText(blogDTO.getTitle());
         if(contentResult.isContainsSensitiveWord() || titleResult.isContainsSensitiveWord())
        {
            Set<String> wordsSet = new HashSet<>();
            contentResult.getSensitiveWords().forEach(wordsSet::add);
            titleResult.getSensitiveWords().forEach(wordsSet::add);
            String msg = String.join(",", wordsSet) ;
            return ApiResponse.error("博文包含敏感词: " + msg);
        }
        blogService.upload(blogDTO);
        return ApiResponse.success();

    }

    @DeleteMapping
    public ApiResponse deleteBlog(@RequestParam Integer id) {
        log.info("delete blog by id {}", id);
        blogService.deleteById(id);
        return ApiResponse.success();
    }

    @PostMapping("/clickLike")
    public ApiResponse clickLike(@RequestBody LikeBlogDTO likeBlogDTO) {
        if (blogService.findLike(likeBlogDTO) == 1) {
            return ApiResponse.success(1);
        } else
            blogService.addLike(likeBlogDTO);
            return ApiResponse.success(0);
    }
    @PutMapping("/removeLike")
    public ApiResponse removeLike(@RequestBody LikeBlogDTO likeBlogDTO) {
        blogService.removeLike(likeBlogDTO);
        return ApiResponse.success();
    }

    @GetMapping("/commentList")
    public ApiResponse<PageResult> getComment(CommentPageQueryDTO commentPageQueryDTO){
        PageResult pageResult =blogService.commentPageQuery(commentPageQueryDTO);
        return ApiResponse.success(pageResult);
    }
    @PostMapping("/addComment")
    public ApiResponse addComment(@RequestBody CommentDTO commentDTO) {
        blogService.addComment(commentDTO);
        return ApiResponse.success();
    }

    @GetMapping("/searchBlogs")
    public ApiResponse<PageResult> searchBlogByPage (SearchBlogDTO searchBlogDTO){
        return ApiResponse.success(blogService.searchBlogByPage(searchBlogDTO));
    }

}
