package com.lxj.myblog.mapper;

import com.lxj.myblog.domain.entity.ChatRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatMapper {
    @Insert("INSERT INTO chat_record (sender_id, receiver_id, message, send_time) VALUES (#{senderId}, #{receiverId}, #{message}, #{sendTime})")
    void insertChatRecord(ChatRecord chatRecord);
    @Select("select  * from chat_record " +
            "where (sender_id = #{senderId} and receiver_id = #{receiverId})" +
            " or (sender_id = #{receiverId} and receiver_id = #{senderId}) order by send_time asc")
    List<ChatRecord> getChatRecordsBySenderAndReceiver(@Param("senderId")Integer senderId,@Param("receiverId") Integer receiverId);
}
