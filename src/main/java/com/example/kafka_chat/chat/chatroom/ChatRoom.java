package com.example.kafka_chat.chat.chatroom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    private Integer id;
    private String name;
    private String password;
    private String createId;
}
