package com.example.kafka_chat.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {
    private final ChatRoomService service;

    @GetMapping("/room-list")
    public List<ChatRoom> listRooms() {
        return service.getAllRooms();
    }

    @PostMapping("/rooms")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody ChatRoom room) {
        service.createRoom(room.getName());
        return ResponseEntity.ok(room); // 또는 생성된 room 객체 반환
    }
}
