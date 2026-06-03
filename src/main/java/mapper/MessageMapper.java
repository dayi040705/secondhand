package com.demo.secondhand.mapper;

import com.demo.secondhand.entity.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageMapper {

    @Insert("INSERT INTO message(from_user_id, to_user_id, product_id, content, send_time, is_read) " +
            "VALUES(#{fromUserId}, #{toUserId}, #{productId}, #{content}, #{sendTime}, #{isRead})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Message message);

    // 查询某用户收到的消息（收件箱，按时间倒序）
    @Select("SELECT * FROM message WHERE to_user_id = #{userId} ORDER BY send_time DESC")
    List<Message> findByToUserId(Integer userId);

    // 查询某用户发出的消息（发件箱）
    @Select("SELECT * FROM message WHERE from_user_id = #{userId} ORDER BY send_time DESC")
    List<Message> findByFromUserId(Integer userId);

    // 标记消息为已读
    @Update("UPDATE message SET is_read = 1 WHERE id = #{id}")
    int markAsRead(Integer id);

    // 查询未读消息数量
    @Select("SELECT COUNT(*) FROM message WHERE to_user_id = #{userId} AND is_read = 0")
    int countUnread(Integer userId);

    @Select("SELECT * FROM message WHERE id = #{id}")
    Message findById(Integer id);
}