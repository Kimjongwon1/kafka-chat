package com.example.kafka_chat.chat.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatProducer chatProducer;
    private final ChatService chatService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody ChatMessage chatMessage) {
        chatProducer.send(chatMessage);
        return ResponseEntity.ok("메시지 전송 완료");
    }
    @GetMapping("/history")
    public List<ChatMessage> getHistory() {
        return chatService.getMessageHistory();
    }
    @GetMapping("/history/{roomId}")
    public List<ChatMessage> getHistoryByRoom(@PathVariable String roomId) {
        return chatService.getMessageHistoryByRoom(roomId);  // 서비스 거쳐감
    }
    @GetMapping("/history/private/{roomId}")
    public List<ChatMessage> getPrivateHistory(@PathVariable String roomId) {
        return chatService.getPrivateMessagesByRoomId(roomId);
    }

    @GetMapping("/rooms")
    public List<String> getAllRoomIds() {
        return chatService.getAllRoomIds(); // 🔥 서비스 통해 조회
    }
    @GetMapping("/room/{roomId}/count")
    public int getParticipantCount(@PathVariable String roomId) {
        return chatService.getParticipantCount(roomId);
    }


}
