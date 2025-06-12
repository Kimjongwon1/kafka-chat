package com.example.kafka_chat.chat.chat;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ChatPrivateMapper {

    // 🔔 ===== 기본 채팅 메서드들 ===== 🔔
    void insertPrivateMessage(ChatMessage message);
    List<ChatMessage> getPrivateMessagesByRoomId(String roomId);
    void deletePrivateRoomById(@Param("roomId") int roomId);

    // 🔥 ===== 읽음 상태 관리 메서드들 (DB 기반) ===== 🔥

    // 채팅방 정보 조회
    ChatPrivateRoom findPrivateRoomById(@Param("roomId") Long roomId);

    // 안읽은 메시지 개수 조회 (LocalDateTime 사용)
    int countUnreadPrivateMessages(@Param("roomId") String roomId, @Param("lastReadTime") LocalDateTime lastReadTime);

    // 읽음 시간 업데이트 (LocalDateTime 사용)
    int updateCreateUserLastRead(@Param("roomId") Long roomId, @Param("readTime") LocalDateTime readTime);
    int updateInviteUserLastRead(@Param("roomId") Long roomId, @Param("readTime") LocalDateTime readTime);

    // 🔔 ===== 유틸리티 메서드들 ===== 🔔

    // 마지막/첫번째 메시지 조회
    ChatMessage getLastPrivateMessage(@Param("roomId") String roomId);
    ChatMessage getFirstPrivateMessage(@Param("roomId") String roomId);

    // 메시지 개수 조회
    int countTotalPrivateMessages(@Param("roomId") String roomId);
    int countPrivateMessagesByUser(@Param("roomId") String roomId, @Param("userId") String userId);

    // 특정 시간 이후 메시지 조회
    List<ChatMessage> getPrivateMessagesAfterTime(@Param("roomId") String roomId, @Param("timestamp") String timestamp);

    // 특정 사용자를 제외한 안읽은 메시지 개수 (String 기반 - 호환성)
    int countUnreadPrivateMessagesExcludingUser(@Param("roomId") String roomId,
                                                @Param("lastReadTime") String lastReadTime,
                                                @Param("userId") String userId);

    // 특정 사용자가 보낸 메시지 중 특정 시간 이후의 개수 (String 기반)
    int countUnreadPrivateMessagesFromUser(@Param("roomId") String roomId,
                                           @Param("sender") String sender,
                                           @Param("timestamp") String timestamp);

    // 🔥 ===== 배치 처리용 메서드들 ===== 🔥

    // 여러 방 정보 한번에 조회
    List<ChatPrivateRoom> findPrivateRoomsByIds(@Param("roomIds") List<Long> roomIds);

    // 특정 사용자가 참여한 모든 방 조회
    List<ChatPrivateRoom> findPrivateRoomsByUserId(@Param("userId") String userId);

    // 방 생성
    int insertPrivateRoom(@Param("name") String name,
                          @Param("password") String password,
                          @Param("createId") String createId,
                          @Param("inviteId") String inviteId);
}