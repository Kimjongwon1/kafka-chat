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

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMapper chatMapper;

    @KafkaListener(topics = "chat-messages", groupId = "chat-group")
    public void listen(ChatMessage message) {
        System.out.println("🔁 Kafka에서 받은 메시지: " + message);

        // ✅ timestamp가 없으면 현재 시각으로 채워줌
        if (message.getTimestamp() == null) {
            message.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        // 1. WebSocket으로 브로드캐스트
        messagingTemplate.convertAndSend("/sub/chat/message", message);

        // 2. DB 저장
        chatMapper.insertChatMessage(message);
    }
}
