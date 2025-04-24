package com.example.kafka_chat.chat;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ChatRoomMapper {

    List<ChatRoom> getAllRooms();  // 전체 채팅방 목록

    void createRoom(ChatRoom chatRoom);  // 채팅방 생성
}
