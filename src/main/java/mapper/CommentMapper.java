package com.demo.secondhand.mapper;

import com.demo.secondhand.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("INSERT INTO comment(product_id, user_id, username, content, create_time) " +
            "VALUES(#{productId}, #{userId}, #{username}, #{content}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Comment comment);

    @Select("SELECT * FROM comment WHERE product_id = #{productId} ORDER BY create_time DESC")
    List<Comment> findByProductId(Integer productId);

    @Delete("DELETE FROM comment WHERE id = #{id}")
    int deleteById(Integer id);

    @Select("SELECT * FROM comment WHERE id = #{id}")
    Comment findById(Integer id);
}