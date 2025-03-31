package com.lxj.myblog.mapper;

import com.github.pagehelper.Page;
import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.entity.Blog;
import com.lxj.myblog.domain.entity.Comment;
import com.lxj.myblog.domain.vo.AuthorVO;
import com.lxj.myblog.domain.vo.BlogVO;
import com.lxj.myblog.domain.vo.MusicVO;
import com.lxj.myblog.result.PageResult;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlogMapper {
    public List<BlogVO> getBlogListByUserId(Integer userId);

    AuthorVO getAuthor(Integer userId);

    @Insert("insert into blog_post (id, title, content, user_id, views, likes, comments, created_at, updated_at)" +
            "values (#{id}, #{title}, #{content}, #{userId}, #{views}, #{likes},#{comments},#{createdAt},#{updatedAt})")
    void insert(Blog blog);

    @Delete("delete from blog_post where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from blog_post where user_id = #{userId} order by created_at desc")
    Page<Blog> pageQuery(BlogPageQueryDTO blogPageQueryDTO);

    @Select("select count(*) from blog_like where user_id = #{userId} and blog_id=#{blogId}")
    int findLike(LikeBlogDTO likeBlogDTO);

    @Insert("insert into blog_like (user_id,blog_id) values(#{userId},#{blogId})")
    void addLike(LikeBlogDTO likeBlogDTO);
    @Delete("delete from blog_like where user_id = #{userId} and blog_id=#{blogId}")
    void removeLike(LikeBlogDTO likeBlogDTO);

    @Update("update blog_post set likes = likes + 1 where id=#{blogId}")
    void addLikeAmount(LikeBlogDTO likeBlogDTO);
    @Update("update blog_post set likes = likes - 1 where id=#{blogId}")
    void removeLikeAmount(LikeBlogDTO likeBlogDTO);
    @Select("select blog_comment.*,users.username,users.avatar from blog_comment left outer join users on blog_comment.user_id = users.user_id where blog_id = #{blogId} order by created_at desc")
    Page<Comment> commentPageQuery(CommentPageQueryDTO commentPageQueryDTO);
    @Update("update blog_post set comments = comments + 1 where id=#{blogId}")
    void addCommentAmount(CommentDTO commentDTO);
    @Insert("insert into blog_comment (blog_id, user_id, content) values (#{blogId}, #{userId}, #{content})")
    void addComment(CommentDTO commentDTO);


}
