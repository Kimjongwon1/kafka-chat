package com.example.kafka_chat.chat.chatroom;

import com.example.kafka_chat.User.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatRoomMapper {

    List<ChatRoom> getAllRooms();  // 전체 채팅방 목록
    void deleteRoomById(@Param("roomId") int roomId);

    void createRoom(ChatRoom chatRoom);  // 채팅방 생성

    void privatecreateRoom(PrivateChatRoom privateChatRoom);

    List<PrivateChatRoom> getMyAllPrivateRooms(String userId);

    List<User> getAllUsers();
    PrivateChatRoom getPrivateRoomById(@Param("roomId") int roomId);
}
