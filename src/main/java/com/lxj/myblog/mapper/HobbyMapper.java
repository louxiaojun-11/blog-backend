package com.lxj.myblog.mapper;

import com.github.pagehelper.Page;
import com.lxj.myblog.domain.dto.*;
import com.lxj.myblog.domain.entity.Blog;
import com.lxj.myblog.domain.entity.Comment;
import com.lxj.myblog.domain.vo.*;
import org.apache.ibatis.annotations.*;

@Mapper
public interface HobbyMapper {
    Page<HobbyGroupVO> groupPageQuery(HobbyGroupPageDTO hobbyGroupPageDTO);
    @Insert("insert into hobby_blog(group_id, user_id, content, title, pic1, pic2, pic3, pic4) values (#{groupId}, #{userId}, #{content}, #{title}, #{pic1}, #{pic2}, #{pic3}, #{pic4})")
    void uploadHobbyBlog(HobbyBlogDTO hobbyBlogDTO);
    @Insert("insert into hobby_group(group_name, user_id, introduce,avatar, type) values (#{groupName}, #{userId}, #{introduce}, #{avatar}, #{type})")
    @Options (useGeneratedKeys = true, keyProperty = "groupId", keyColumn = "group_id")
    Integer createGroup(GroupCreateDTO groupCreateDTO);
    @Insert("insert into group_member(group_id, user_id) values (#{groupId}, #{userId})")
    void joinGroup(JoinGroupDTO joinGroupDTO);
    @Update(("update hobby_group set members = members + 1 where group_id = #{groupId}"))
    void addGroupMember(JoinGroupDTO joinGroupDTO);
    @Select("select hobby_blog.blog_id,title,likes,comments,content,hobby_blog.created_at, username from hobby_blog join users on hobby_blog.user_id = users.user_id   where group_id = #{groupId} order by hobby_blog.created_at desc")
    Page<GroupBlogListVO> groupBlogListPageQuery(GroupBlogListPageDTO groupBlogListPageDTO);
    @Select("select group_name, avatar, introduce,type,members from hobby_group where group_id = #{groupId}")
    GroupInfoVO getGroupInfo(Integer groupId);
    @Select ("select count(*) from group_member where group_id = #{groupId} and user_id = #{userId}")
    Integer isGroupMember( @Param("groupId") Integer groupId,
                           @Param("userId") Integer userId);
    @Delete("delete from group_member where group_id = #{groupId} and user_id = #{userId}")
    void quitGroup(JoinGroupDTO joinGroupDTO);
    @Update("update hobby_group set members = members - 1 where group_id = #{groupId}")
    void reduceGroupMember(JoinGroupDTO joinGroupDTO);
    @Select("select * from hobby_blog where blog_id = #{blogId}")
    GroupBlogDetailVO getGroupBlogDetail(Integer blogId);
    @Select("select username,avatar from users where users.user_id = #{userId}")
    GroupBlogUserVO getGroupBlogUser(Integer userId);
    @Select("select count(*) from hobby_like where user_id = #{userId} and blog_id=#{blogId}")
    int findLike(LikeBlogDTO likeBlogDTO);
    @Update("update hobby_blog set likes = likes + 1 where blog_id=#{blogId}")
    void addLikeAmount(LikeBlogDTO likeBlogDTO);
    @Insert("insert into hobby_like (user_id,blog_id) values(#{userId},#{blogId})")
    void addLike(LikeBlogDTO likeBlogDTO);
    @Update("update hobby_blog set likes = likes - 1 where blog_id=#{blogId}")
    void removeLikeAmount(LikeBlogDTO likeBlogDTO);
    @Delete("delete from hobby_like where user_id = #{userId} and blog_id=#{blogId}")
    void removeLike(LikeBlogDTO likeBlogDTO);
    @Select("select hobby_comment.*,users.username,users.avatar from hobby_comment left outer join users on hobby_comment.user_id = users.user_id where blog_id = #{blogId} order by created_at desc")
    Page<Comment> commentPageQuery(CommentPageQueryDTO commentPageQueryDTO);
    @Update("update hobby_blog set comments = comments + 1 where blog_id=#{blogId}")
    void addCommentAmount(CommentDTO commentDTO);
    @Insert("insert into hobby_comment (blog_id, user_id, content) values (#{blogId}, #{userId}, #{content})")
    void addComment(CommentDTO commentDTO);
    @Select("select b.blog_id,b.title,b.content,b.likes,b.comments,b.created_at,b.user_id,b.group_id,u.username,g.group_name,g.type" +
            " from hobby_blog b " +
            "join users u on b.user_id = u.user_id " +
            "join hobby_group g on b.group_id = g.group_id " +
            "where b.user_id = #{userId} order by b.created_at desc")
    Page<UserHobbyBlogVO> userHobbyBlogPageQuery(BlogPageQueryDTO blogPageQueryDTO);
}
