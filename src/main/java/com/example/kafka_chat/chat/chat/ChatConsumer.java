package com.example.kafka_chat.chat.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatConsumer {

    private final ChatService chatService;

    @KafkaListener(topics = "chat-messages", groupId = "chat-group")
    public void listen(ChatMessage message) {
        chatService.saveAndBroadcast(message);
    }
}
