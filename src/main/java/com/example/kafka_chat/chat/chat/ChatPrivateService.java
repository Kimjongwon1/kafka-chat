package com.example.kafka_chat.chat.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatPrivateService {

    private final ChatPrivateMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;

    public void saveAndBroadcast(ChatMessage message) {
        if (message.getTimestamp() == null || message.getTimestamp().isBlank()) {
            message.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        mapper.insertPrivateMessage(message);
        messagingTemplate.convertAndSend("/sub/chat/private/" + message.getRoomId(), message);
    }

    public List<ChatMessage> getHistoryByRoom(String roomId) {
        return mapper.getPrivateMessagesByRoomId(roomId);
    }

    public void deletePrivateRoomById(int roomId) {
        mapper.deletePrivateRoomById(roomId);
    }

    // 🔥 ===== DB 기반 안읽은 메시지 관리 (간단한 버전) ===== 🔥

    /**
     * 안읽은 메시지 개수 조회 (DB 기준)
     */
    public int getUnreadMessageCount(String roomId, String userId) {
        try {
            Long roomIdLong = Long.parseLong(roomId);
            ChatPrivateRoom room = mapper.findPrivateRoomById(roomIdLong);

            if (room == null) {
                throw new RuntimeException("존재하지 않는 채팅방입니다.");
            }

            LocalDateTime lastRead = null;

            // 사용자 역할에 따라 읽음 시간 가져오기
            if (userId.equals(room.getCreateId())) {
                lastRead = room.getCreateUserLastRead();
            } else if (userId.equals(room.getInviteId())) {
                lastRead = room.getInviteUserLastRead();
            } else {
                throw new RuntimeException("해당 사용자는 이 방의 참가자가 아닙니다.");
            }

            // null이면 모든 메시지가 안읽음
            if (lastRead == null) {
                lastRead = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
            }

            log.debug("🔔 안읽은 메시지 개수 조회 - roomId: {}, userId: {}, lastRead: {}", roomId, userId, lastRead);

            int count = mapper.countUnreadPrivateMessages(roomId, lastRead);

            log.debug("✅ 안읽은 메시지 개수: {}", count);
            return count;

        } catch (Exception e) {
            log.error("❌ 안읽은 메시지 개수 조회 실패 - roomId: {}, userId: {}, error: {}", roomId, userId, e.getMessage());
            return 0;
        }
    }

    /**
     * 읽음 처리 (DB 업데이트)
     */
    public void markAsRead(String roomId, String userId) {
        try {
            Long roomIdLong = Long.parseLong(roomId);
            ChatPrivateRoom room = mapper.findPrivateRoomById(roomIdLong);

            if (room == null) {
                throw new RuntimeException("존재하지 않는 채팅방입니다.");
            }

            LocalDateTime currentTime = LocalDateTime.now();

            int updatedRows = 0;
            if (userId.equals(room.getCreateId())) {
                updatedRows = mapper.updateCreateUserLastRead(roomIdLong, currentTime);
                log.info("✅ 생성자 읽음 시간 업데이트 - roomId: {}, userId: {}", roomId, userId);
            } else if (userId.equals(room.getInviteId())) {
                updatedRows = mapper.updateInviteUserLastRead(roomIdLong, currentTime);
                log.info("✅ 초대자 읽음 시간 업데이트 - roomId: {}, userId: {}", roomId, userId);
            } else {
                throw new RuntimeException("해당 사용자는 이 방의 참가자가 아닙니다.");
            }

            if (updatedRows == 0) {
                log.warn("⚠️ 읽음 시간 업데이트 실패 - roomId: {}, userId: {}", roomId, userId);
            }

        } catch (Exception e) {
            log.error("❌ 읽음 처리 실패 - roomId: {}, userId: {}, error: {}", roomId, userId, e.getMessage());
            throw e;
        }
    }

    /**
     * 마지막 메시지 조회
     */
    public Map<String, Object> getLastMessage(String roomId) {
        try {
            ChatMessage lastMessage = mapper.getLastPrivateMessage(roomId);

            if (lastMessage == null) {
                return new HashMap<>();
            }

            Map<String, Object> result = new HashMap<>();
            result.put("message", lastMessage.getMessage());
            result.put("timestamp", lastMessage.getTimestamp());
            result.put("senderId", lastMessage.getSender());
            result.put("senderName", lastMessage.getSender());

            return result;
        } catch (Exception e) {
            log.error("❌ 마지막 메시지 조회 실패 - roomId: {}, error: {}", roomId, e.getMessage());
            return new HashMap<>();
        }
    }

    // 🔔 ===== 기타 유틸리티 메서드들 ===== 🔔

    public int getTotalMessageCount(String roomId) {
        try {
            return mapper.countTotalPrivateMessages(roomId);
        } catch (Exception e) {
            log.error("❌ 총 메시지 개수 조회 실패 - roomId: {}", roomId);
            return 0;
        }
    }

    public Map<String, Object> getRoomActivity(String roomId) {
        try {
            Map<String, Object> activity = new HashMap<>();

            int totalMessages = mapper.countTotalPrivateMessages(roomId);
            ChatMessage lastMessage = mapper.getLastPrivateMessage(roomId);
            ChatMessage firstMessage = mapper.getFirstPrivateMessage(roomId);

            activity.put("totalMessages", totalMessages);
            activity.put("lastMessageTime", lastMessage != null ? lastMessage.getTimestamp() : null);
            activity.put("firstMessageTime", firstMessage != null ? firstMessage.getTimestamp() : null);
            activity.put("roomId", roomId);

            return activity;
        } catch (Exception e) {
            log.error("❌ 방 활동 정보 조회 실패 - roomId: {}", roomId);
            return Map.of("error", e.getMessage());
        }
    }
}