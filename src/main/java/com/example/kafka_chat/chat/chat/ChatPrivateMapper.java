package com.example.kafka_chat.chat.chat;

import org.apache.ibatis.annotations.Mapper;
import com.example.kafka_chat.chat.chat.ChatMessage;

import java.util.List;

@Mapper
public interface ChatPrivateMapper {

    // 메시지 저장
    void insertPrivateMessage(ChatMessage message);

    // 특정 방의 메시지 전체 조회
    List<ChatMessage> getPrivateMessagesByRoomId(String roomId);
}
