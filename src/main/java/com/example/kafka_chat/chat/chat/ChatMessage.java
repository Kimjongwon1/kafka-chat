package com.example.kafka_chat.chat.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private Long id;
    private String sender;
    private String message;
    private String timestamp;
    private String roomId;
}
