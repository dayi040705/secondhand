package com.demo.secondhand.mapper;

import com.demo.secondhand.entity.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Insert("INSERT INTO product(title, description, price, category, image_path, user_id, status, create_time) " +
            "VALUES(#{title}, #{description}, #{price}, #{category}, #{imagePath}, #{userId}, #{status}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Product product);

    @Select("SELECT * FROM product WHERE status = 1 ORDER BY create_time DESC")
    List<Product> findAllOnSale();

    @Select("SELECT * FROM product WHERE user_id = #{userId}")
    List<Product> findByUserId(Integer userId);

    @Select("SELECT * FROM product WHERE id = #{id}")
    Product findById(Integer id);

    @Update("UPDATE product SET title=#{title}, description=#{description}, price=#{price}, category=#{category}, image_path=#{imagePath} WHERE id=#{id}")
    int update(Product product);

    @Delete("DELETE FROM product WHERE id=#{id}")
    int deleteById(Integer id);

    @Select("SELECT * FROM product WHERE title LIKE CONCAT('%', #{keyword}, '%') AND status = 1 ORDER BY create_time DESC")
    List<Product> searchByTitle(String keyword);

    @Select("SELECT * FROM product WHERE status = 1 AND category = #{category} ORDER BY create_time DESC")
    List<Product> findByCategory(String category);

    @Select("SELECT COUNT(*) FROM product")
    int countAll();

    @Select("SELECT * FROM product ORDER BY create_time DESC")
    List<Product> findAll();

    @Update("UPDATE product SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
}