package com.example.kafka_chat.chat.chatjoin;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class ChatJoinController {

    private final SimpMessagingTemplate messagingTemplate;

    // 메모리에 간단하게 참여자 저장 (실서비스면 Redis나 DB 사용해야 함)
    private final Map<String, Set<String>> roomParticipants = new HashMap<>();

    @MessageMapping("/chat/join")
    public void handleJoin(JoinMessage joinMessage) {
        String roomId = joinMessage.getRoomId();
        String username = joinMessage.getUsername();

        roomParticipants.computeIfAbsent(roomId, k -> new HashSet<>()).add(username);

        // 참여자 수 브로드캐스트
        int count = roomParticipants.get(roomId).size();
        messagingTemplate.convertAndSend("/sub/chat/participants/" + roomId, count);
    }
}
