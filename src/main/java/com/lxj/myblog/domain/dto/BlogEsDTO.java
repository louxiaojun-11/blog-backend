package com.lxj.myblog.domain.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxj.myblog.domain.entity.Blog;
import com.lxj.myblog.domain.vo.AuthorVO;
import com.lxj.myblog.domain.vo.BlogVO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.beans.BeanUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Document(indexName = "blog_index")
@Data
public class BlogEsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    /**
     * 内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;

    /**
     * 用户 ID
     */
    private Integer userId;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 浏览量
     */
    private Integer views;

    /**
     * 评论数
     */
    private Integer comments;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * 作者信息
     */
    private AuthorVO author;

    /**
     * 对象转包装类
     *
     * @param
     * @return
     */
    public static BlogEsDTO objToDto(BlogVO blogVO) {
        if (blogVO == null) {
            return null;
        }
        BlogEsDTO blogEsDTO = new BlogEsDTO();
        BeanUtils.copyProperties(blogVO, blogEsDTO);
        return blogEsDTO;
    }

    /**
     * 包装类转对象
     *
     * @param blogEsDTO
     * @return
     */
    public static BlogVO dtoToObj(BlogEsDTO blogEsDTO) {
        if (blogEsDTO == null) {
            return null;
        }
        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(blogEsDTO, blogVO);
        return blogVO;
    }
}
