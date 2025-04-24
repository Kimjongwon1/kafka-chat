package com.example.kafka_chat.chat.chatjoin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JoinMessage {
    private String roomId;
    private String username;
}
