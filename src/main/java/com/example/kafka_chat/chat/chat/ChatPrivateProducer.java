package com.example.kafka_chat.chat.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatPrivateProducer {
    private final KafkaTemplate<String, ChatMessage> kafkaTemplate;

    public void send(ChatMessage message) {
        kafkaTemplate.send("private-chat-messages", message);
    }
}
