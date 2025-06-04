package com.example.kafka_chat.chat.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

// ChatPrivateConsumer.java
@Service
@RequiredArgsConstructor
public class ChatPrivateConsumer {

    private final ChatPrivateService chatPrivateService;

    @KafkaListener(topics = "private-chat-messages", groupId = "chat-group")
    public void listen(ChatMessage message) {
        chatPrivateService.saveAndBroadcast(message);
    }
}
