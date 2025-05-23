package com.example.kafka_chat.chat.chat;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ChatMapper {
    void insertChatMessage(ChatMessage message);
    List<ChatMessage> getAllMessages();
    List<ChatMessage> getMessagesByRoomId(String roomId);
    List<String> getAllRoomIds();
    int countParticipantsInRoom(String roomId);

    List<ChatMessage> getPrivateMessagesByRoomId(String roomId);
}
