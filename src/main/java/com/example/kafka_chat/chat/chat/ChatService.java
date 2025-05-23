package com.example.kafka_chat.chat.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMapper chatMapper;
    private final ChatPrivateMapper chatPrivateMapper;
    private final SimpMessagingTemplate messagingTemplate;

    public void saveAndBroadcast(ChatMessage message) {
        if (message.getTimestamp() == null) {
            message.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        messagingTemplate.convertAndSend("/sub/chat/" + message.getRoomId(), message); // ✅ roomId 기반으로 변경
        chatMapper.insertChatMessage(message); // DB 저장
    }


    public List<ChatMessage> getMessageHistory() {
        return chatMapper.getAllMessages();
    }
    public List<ChatMessage> getMessageHistoryByRoom(String roomId) {
        return chatMapper.getMessagesByRoomId(roomId);
    }
    // ChatService.java
    public List<String> getAllRoomIds() {
        return chatMapper.getAllRoomIds();
    }
    public int getParticipantCount(String roomId) {
        return chatMapper.countParticipantsInRoom(roomId);
    }

    public List<ChatMessage> getPrivateMessagesByRoomId(String roomId) {
        return chatPrivateMapper.getPrivateMessagesByRoomId(roomId);
    }
}
