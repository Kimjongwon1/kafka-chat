package com.example.kafka_chat.controller;

import com.example.kafka_chat.chat.ChatMessage;
import com.example.kafka_chat.chat.ChatProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatStompController {

    private final ChatProducer chatProducer;

    @MessageMapping("/chat/message") // 클라이언트가 /pub/chat/message 로 보내면
    public void handleMessage(ChatMessage message) {
        message.setTimestamp(java.time.LocalDateTime.now().toString()); // 타임스탬프 설정
        chatProducer.send(message); // Kafka로 전송
    }
}
