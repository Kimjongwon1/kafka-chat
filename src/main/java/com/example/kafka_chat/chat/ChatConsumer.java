package com.example.kafka_chat.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Service
@RequiredArgsConstructor
public class ChatConsumer {

    private final ChatService chatService;

    @KafkaListener(topics = "chat-messages", groupId = "chat-group")
    public void listen(ChatMessage message) {
        chatService.saveAndBroadcast(message);
    }
}
