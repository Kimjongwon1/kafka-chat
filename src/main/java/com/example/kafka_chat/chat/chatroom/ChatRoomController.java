package com.example.kafka_chat.chat.chatroom;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room") // ✅ 완전 별도 경로로
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService service;

    @GetMapping("/list")
    public List<ChatRoom> listRooms() {
        return service.getAllRooms();
    }

    @PostMapping("/create")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody ChatRoom room) {
        service.createRoom(room.getName());
        return ResponseEntity.ok(room);
    }
    @DeleteMapping("/delete/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable int roomId) {
        service.deleteRoomById(roomId);
        return ResponseEntity.ok("삭제 완료");
    }

}
