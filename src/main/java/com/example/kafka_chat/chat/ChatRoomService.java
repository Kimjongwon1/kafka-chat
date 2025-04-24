package com.example.kafka_chat.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomMapper mapper;

    public List<ChatRoom> getAllRooms() {
        return mapper.getAllRooms();
    }

    public void createRoom(String name) {
        ChatRoom room = new ChatRoom();
        room.setName(name);
        mapper.createRoom(room); // 생성된 ID는 room.getId()로 꺼낼 수 있음
    }

}
