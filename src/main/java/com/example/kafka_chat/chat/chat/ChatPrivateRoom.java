package com.example.kafka_chat.chat.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatPrivateRoom {
    private Long id;
    private String name;
    private String password;
    private String createId;
    private String inviteId;
    private LocalDateTime createUserLastRead;  // 🔥 방 생성자 마지막 읽은 시간
    private LocalDateTime inviteUserLastRead;  // 🔥 초대받은 사용자 마지막 읽은 시간
    private LocalDateTime createdAt;
}