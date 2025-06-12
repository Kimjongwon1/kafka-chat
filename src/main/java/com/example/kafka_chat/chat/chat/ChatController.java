package com.example.kafka_chat.chat.chat;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private final ChatProducer chatProducer;
    private final ChatService chatService;
    private final ChatPrivateService chatPrivateService;

    // 🔔 ===== 기본 채팅 메서드들 ===== 🔔

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
        return chatService.getMessageHistoryByRoom(roomId);
    }

    @GetMapping("/history/private/{roomId}")
    public List<ChatMessage> getPrivateHistory(@PathVariable String roomId) {
        return chatService.getPrivateMessagesByRoomId(roomId);
    }

    @GetMapping("/rooms")
    public List<String> getAllRoomIds() {
        return chatService.getAllRoomIds();
    }

    @GetMapping("/room/{roomId}/count")
    public int getParticipantCount(@PathVariable String roomId) {
        return chatService.getParticipantCount(roomId);
    }

    // 🔥 ===== 개인 채팅방 안읽은 메시지 관리 (DB 기반) ===== 🔥

    /**
     * 개인 채팅방 안읽은 메시지 개수 조회 (DB 기준)
     */
    @GetMapping("/private/unread/count")
    public ResponseEntity<Map<String, Object>> getPrivateUnreadMessageCount(
            @RequestParam String roomId,
            @RequestParam String userId) {

        try {
            log.info("🔔 개인 채팅방 안읽은 메시지 개수 조회 - roomId: {}, userId: {}", roomId, userId);

            // 입력값 검증
            if (roomId == null || roomId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "roomId는 필수입니다."));
            }
            if (userId == null || userId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "userId는 필수입니다."));
            }

            int count = chatPrivateService.getUnreadMessageCount(roomId, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("count", count);
            response.put("roomId", roomId);
            response.put("userId", userId);
            response.put("timestamp", System.currentTimeMillis());

            log.info("✅ 개인 채팅방 안읽은 메시지 개수: {}", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("❌ 개인 채팅방 안읽은 메시지 개수 조회 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 개인 채팅방 읽음 처리 (DB 업데이트)
     */
    @PostMapping("/private/mark-as-read")
    public ResponseEntity<Map<String, Object>> markPrivateRoomAsRead(
            @RequestBody Map<String, String> request) {

        try {
            String roomId = request.get("roomId");
            String userId = request.get("userId");

            log.info("🔔 개인 채팅방 읽음 처리 - roomId: {}, userId: {}", roomId, userId);

            // 입력값 검증
            if (roomId == null || roomId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "roomId는 필수입니다."));
            }
            if (userId == null || userId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "userId는 필수입니다."));
            }

            chatPrivateService.markAsRead(roomId, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "읽음 처리 완료");
            response.put("roomId", roomId);
            response.put("userId", userId);
            response.put("timestamp", System.currentTimeMillis());

            log.info("✅ 개인 채팅방 읽음 처리 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("❌ 개인 채팅방 읽음 처리 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 개인 채팅방 마지막 메시지 조회
     */
    @GetMapping("/private/last-message")
    public ResponseEntity<Map<String, Object>> getPrivateLastMessage(@RequestParam String roomId) {
        try {
            log.info("🔔 개인 채팅방 마지막 메시지 조회 - roomId: {}", roomId);

            Map<String, Object> lastMessage = chatPrivateService.getLastMessage(roomId);

            log.info("✅ 개인 채팅방 마지막 메시지 조회 성공");
            return ResponseEntity.ok(lastMessage);
        } catch (Exception e) {
            log.error("❌ 개인 채팅방 마지막 메시지 조회 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 개인 채팅방 배치 안읽은 메시지 개수 조회
     */
    @PostMapping("/private/unread/batch")
    public ResponseEntity<Map<String, Object>> getBatchPrivateUnreadCounts(
            @RequestBody Map<String, Object> request) {

        try {
            @SuppressWarnings("unchecked")
            List<String> roomIds = (List<String>) request.get("roomIds");
            String userId = (String) request.get("userId");

            log.info("🔔 배치 개인 채팅방 안읽은 메시지 개수 조회 - roomIds: {}, userId: {}",
                    roomIds != null ? roomIds.size() : 0, userId);

            if (roomIds == null || roomIds.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "roomIds는 필수입니다."));
            }
            if (userId == null || userId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "userId는 필수입니다."));
            }

            Map<String, Integer> unreadCounts = new HashMap<>();

            // 각 방에 대해 안읽은 메시지 개수 조회
            for (String roomId : roomIds) {
                try {
                    int count = chatPrivateService.getUnreadMessageCount(roomId.trim(), userId.trim());
                    unreadCounts.put(roomId, count);
                } catch (Exception e) {
                    log.warn("⚠️ 방 {} 안읽은 메시지 개수 조회 실패: {}", roomId, e.getMessage());
                    unreadCounts.put(roomId, 0);
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("unreadCounts", unreadCounts);
            response.put("userId", userId);
            response.put("timestamp", System.currentTimeMillis());

            log.info("✅ 배치 개인 채팅방 안읽은 메시지 개수 조회 완료 - 처리된 방: {}", unreadCounts.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("❌ 배치 개인 채팅방 안읽은 메시지 개수 조회 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 🔔 ===== 일반 채팅방 메서드들 (임시 구현) ===== 🔔

    @GetMapping("/unread/count")
    public ResponseEntity<Map<String, Object>> getUnreadMessageCount(
            @RequestParam String roomId,
            @RequestParam long lastReadTime) {

        try {
            log.info("🔔 일반 채팅방 안읽은 메시지 개수 조회 - roomId: {}, lastReadTime: {}", roomId, lastReadTime);

            // TODO: ChatService에 해당 메서드 구현 필요
            int count = 0; // 임시

            Map<String, Object> response = new HashMap<>();
            response.put("count", count);
            response.put("roomId", roomId);
            response.put("lastReadTime", lastReadTime);

            log.info("✅ 일반 채팅방 안읽은 메시지 개수: {}", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("❌ 일반 채팅방 안읽은 메시지 개수 조회 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/last-message")
    public ResponseEntity<Map<String, Object>> getLastMessage(@RequestParam String roomId) {
        try {
            log.info("🔔 일반 채팅방 마지막 메시지 조회 - roomId: {}", roomId);

            // TODO: ChatService에 해당 메서드 구현 필요
            Map<String, Object> lastMessage = new HashMap<>(); // 임시

            log.info("✅ 일반 채팅방 마지막 메시지 조회 성공");
            return ResponseEntity.ok(lastMessage);
        } catch (Exception e) {
            log.error("❌ 일반 채팅방 마지막 메시지 조회 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/mark-as-read")
    public ResponseEntity<Map<String, Object>> markAsRead(
            @RequestBody Map<String, String> request) {

        try {
            String roomId = request.get("roomId");
            String userId = request.get("userId");

            log.info("🔔 일반 채팅방 읽음 처리 - roomId: {}, userId: {}", roomId, userId);

            // TODO: ChatService에 해당 메서드 구현 필요

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "읽음 처리 완료");
            response.put("roomId", roomId);
            response.put("userId", userId);

            log.info("✅ 일반 채팅방 읽음 처리 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("❌ 일반 채팅방 읽음 처리 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/unread/batch")
    public ResponseEntity<Map<String, Object>> getBatchUnreadCounts(
            @RequestBody Map<String, Object> request) {

        try {
            @SuppressWarnings("unchecked")
            List<String> roomIds = (List<String>) request.get("roomIds");

            log.info("🔔 배치 일반 채팅방 안읽은 메시지 개수 조회 - rooms: {}", roomIds != null ? roomIds.size() : 0);

            Map<String, Integer> unreadCounts = new HashMap<>();
            if (roomIds != null) {
                for (String roomId : roomIds) {
                    // TODO: ChatService에 해당 메서드 구현 필요
                    unreadCounts.put(roomId, 0); // 임시로 0 설정
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("unreadCounts", unreadCounts);
            response.put("roomCount", roomIds != null ? roomIds.size() : 0);

            log.info("✅ 배치 일반 채팅방 안읽은 메시지 개수 조회 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("❌ 배치 일반 채팅방 안읽은 메시지 개수 조회 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 🔔 ===== 디버깅/관리용 메서드들 ===== 🔔

    /**
     * 채팅방 활동 정보 조회 (디버깅용)
     */
    @GetMapping("/private/activity")
    public ResponseEntity<Map<String, Object>> getPrivateRoomActivity(@RequestParam String roomId) {
        try {
            Map<String, Object> activity = chatPrivateService.getRoomActivity(roomId);
            return ResponseEntity.ok(activity);
        } catch (Exception e) {
            log.error("❌ 개인 채팅방 활동 정보 조회 실패 - roomId: {}", roomId);
            return ResponseEntity.badRequest().body(Map.of("error", "활동 정보 조회에 실패했습니다."));
        }
    }
}