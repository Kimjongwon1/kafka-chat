package com.example.kafka_chat.chat.chatjoin;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

        if (roomId == null || roomId.trim().isEmpty() || username == null || username.trim().isEmpty()) {
            System.out.println("❌ 유효하지 않은 roomId 또는 username");
            return;
        }

        roomParticipants.computeIfAbsent(roomId, k -> new HashSet<>()).add(username);

        Set<String> users = roomParticipants.get(roomId);
        int count = (users != null) ? users.size() : 0;

        System.out.println("🟢 join 호출됨: " + username + " / roomId: " + roomId);
//        System.out.println("현재 참여자 목록: " + users);
//        System.out.println("👥 브로드캐스트할 참여자 수: " + count);

        messagingTemplate.convertAndSend("/sub/chat/participants/" + roomId, count);
    }

    @MessageMapping("/chat/leave")
    public void handleLeave(JoinMessage leaveMessage) {
        String roomId = leaveMessage.getRoomId();
        String username = leaveMessage.getUsername();

        if (roomParticipants.containsKey(roomId)) {
            roomParticipants.get(roomId).remove(username);

            // 0명이 되면 map에서 제거 (메모리 최적화 목적)
            if (roomParticipants.get(roomId).isEmpty()) {
                roomParticipants.remove(roomId);
            }
        }

        // 인원 수 브로드캐스트
        int count = roomParticipants.getOrDefault(roomId, Set.of()).size();
        messagingTemplate.convertAndSend("/sub/chat/participants/" + roomId, count);
    }
    @GetMapping("/api/chat/participants/count")
    @ResponseBody
    public int getParticipantCount(@RequestParam String roomId) {
        return roomParticipants.getOrDefault(roomId, Set.of()).size();
    }
}
