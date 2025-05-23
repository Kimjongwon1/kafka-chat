package com.example.kafka_chat.chat.chatroom;

import com.example.kafka_chat.User.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/room") // ✅ 완전 별도 경로로
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService service;

    @GetMapping("/list")
    public List<ChatRoom> listRooms() {
        return service.getAllRooms();
    }
    @GetMapping("/users")
    public List<User> Userlist() {
        return service.getAllUsers();
    }
    @GetMapping("/myprivateroom/list")
    public List<PrivateChatRoom> privatelistRooms(@RequestParam String userId) {
        return service.getMyAllPrivateRooms(userId);
    }

    @PostMapping("/create")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody ChatRoom room) {
        service.createRoom(room.getName(),room.getPassword(), room.getCreateId());
        return ResponseEntity.ok(room);
    }
    @PostMapping("/private/create")
    public ResponseEntity<Map<String, Object>> privateroomcreateRoom(@RequestBody PrivateChatRoom room) {
        int createdId = service.privatecreateRoom(room.getName(), room.getPassword(), room.getCreateId(), room.getInviteId());
        return ResponseEntity.ok(Map.of("roomId", createdId));
    }


    @DeleteMapping("/delete/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable int roomId) {
        service.deleteRoomById(roomId);
        return ResponseEntity.ok("삭제 완료");
    }

}
