package com.example.kafka_chat.chat.chatroom;

import com.example.kafka_chat.User.entity.User;
import com.example.kafka_chat.chat.chat.ChatPrivateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService service;
    private final ChatPrivateService privateService;
    private final SimpMessagingTemplate messagingTemplate; // 추가

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
        PrivateChatRoom createdRoom = new PrivateChatRoom(
                createdId,
                room.getName(),
                room.getPassword(),
                room.getCreateId(),
                room.getInviteId()
        );

        // 초대받은 사용자의 구독 채널로 방 정보 전송
        messagingTemplate.convertAndSend("/sub/private-room/invite/" + room.getInviteId(), createdRoom);
        return ResponseEntity.ok(Map.of("roomId", createdId));
    }

    @DeleteMapping("/private/delete/{roomId}")
    public ResponseEntity<String> PrivatedeleteRoom(@PathVariable int roomId) {
        PrivateChatRoom room = service.getPrivateRoomById(roomId);
//        System.out.println("🔍 삭제할 방 정보: " + room);
        privateService.deletePrivateRoomById(roomId);
        // 알림 전송
        if (room != null) {
            Map<String, Object> deleteNotification = Map.of(
                    "action", "delete",
                    "roomId", roomId
            );
//            System.out.println("📤 삭제 알림 전송: createId=" + room.getCreateId() + ", inviteId=" + room.getInviteId());

            messagingTemplate.convertAndSend("/sub/private-room/invite/" + room.getCreateId(), deleteNotification);
            messagingTemplate.convertAndSend("/sub/private-room/invite/" + room.getInviteId(), deleteNotification);
        }
        return ResponseEntity.ok("삭제 완료");
    }
    @DeleteMapping("/delete/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable int roomId) {
        service.deleteRoomById(roomId);
        return ResponseEntity.ok("삭제 완료");
    }

}
