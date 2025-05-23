package com.example.kafka_chat.controller;

import com.example.kafka_chat.chat.chat.ChatMessage;
import com.example.kafka_chat.chat.chat.ChatPrivateProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

// ChatPrivateStompController.java
@Controller
@RequiredArgsConstructor
public class ChatPrivateStompController {

    private final ChatPrivateProducer chatPrivateProducer;

    @MessageMapping("/chat/private/message")
    public void handlePrivateMessage(ChatMessage message) {
        System.out.println("🔔 수신된 개인 메시지: " + message);
        chatPrivateProducer.send(message);
    }
}
