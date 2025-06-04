package com.example.kafka_chat.chat.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatPrivateService {

    private final ChatPrivateMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;

    public void saveAndBroadcast(ChatMessage message) {
        if (message.getTimestamp() == null || message.getTimestamp().isBlank()) {
            message.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        mapper.insertPrivateMessage(message);
        messagingTemplate.convertAndSend("/sub/chat/private/" + message.getRoomId(), message);
    }

    public List<ChatMessage> getHistoryByRoom(String roomId) {
        return mapper.getPrivateMessagesByRoomId(roomId);
    }
    public void deletePrivateRoomById(int roomId){
        mapper.deletePrivateRoomById(roomId);
    };
}
