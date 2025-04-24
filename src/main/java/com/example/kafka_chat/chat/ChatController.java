package com.example.kafka_chat.chat;

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
}
