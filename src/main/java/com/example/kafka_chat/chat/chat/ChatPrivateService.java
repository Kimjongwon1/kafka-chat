package com.example.kafka_chat.chat.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatPrivateService {

    private final ChatPrivateMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;

    public void saveAndBroadcast(ChatMessage message) {
        mapper.insertPrivateMessage(message);
        messagingTemplate.convertAndSend("/sub/chat/private/" + message.getRoomId(), message);
    }

    public List<ChatMessage> getHistoryByRoom(String roomId) {
        return mapper.getPrivateMessagesByRoomId(roomId);
    }
}
