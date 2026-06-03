package com.demo.secondhand.mapper;

import com.demo.secondhand.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO orders(order_no, product_id, buyer_id, seller_id, price, status, create_time) " +
            "VALUES(#{orderNo}, #{productId}, #{buyerId}, #{sellerId}, #{price}, #{status}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    @Select("SELECT * FROM orders WHERE buyer_id = #{buyerId} ORDER BY create_time DESC")
    List<Order> findByBuyerId(Integer buyerId);

    @Select("SELECT * FROM orders WHERE seller_id = #{sellerId} ORDER BY create_time DESC")
    List<Order> findBySellerId(Integer sellerId);

    @Select("SELECT * FROM orders WHERE order_no = #{orderNo}")
    Order findByOrderNo(String orderNo);

    @Update("UPDATE orders SET status = #{status}, confirm_time = #{confirmTime} WHERE id = #{id}")
    int updateStatus(Order order);

    @Select("SELECT * FROM orders WHERE product_id = #{productId} AND status IN (0, 1)")
    List<Order> findActiveByProductId(Integer productId);
}