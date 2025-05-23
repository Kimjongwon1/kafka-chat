package com.example.kafka_chat.chat.chatroom;

import com.example.kafka_chat.User.entity.User;
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

    public void createRoom(String name, String password, String createId) {
        ChatRoom room = new ChatRoom();
        room.setName(name);
        room.setPassword(password);
        room.setCreateId(createId);
        mapper.createRoom(room); // 생성된 ID는 room.getId()로 꺼낼 수 있음
    }
    public void deleteRoomById(int roomId) {
        mapper.deleteRoomById(roomId);
    }

    public int  privatecreateRoom(String name, String password, String createId, String inviteId) {
        PrivateChatRoom room = new PrivateChatRoom();
        room.setName(name);
        room.setPassword(password);
        room.setCreateId(createId);
        room.setInviteId(inviteId);
        mapper.privatecreateRoom(room);
        return room.getId();
    }

    public List<PrivateChatRoom> getMyAllPrivateRooms(String userId) {
        return mapper.getMyAllPrivateRooms(userId);
    }

    public List<User> getAllUsers() {
        return mapper.getAllUsers();
    }
}
