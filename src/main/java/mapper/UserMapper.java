package com.demo.secondhand.mapper;

import com.demo.secondhand.entity.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user(username, password, contact, create_time) VALUES(#{username}, #{password}, #{contact}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Integer id);

    @Select("SELECT COUNT(*) FROM user")
    int countAll();

    @Select("SELECT * FROM user")
    List<User> findAll();

    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteById(Integer id);

    @Update("UPDATE user SET verified = #{verified} WHERE id = #{id}")
    int updateVerified(@Param("id") Integer id, @Param("verified") Integer verified);
}