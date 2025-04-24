package com.example.kafka_chat.chat.chatjoin;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ParticipantService {

    private final Map<String, Set<String>> roomParticipants = new ConcurrentHashMap<>();

    public void addUserToRoom(String roomId, String username) {
        roomParticipants.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(username);
    }

    public int getParticipantCount(String roomId) {
        return roomParticipants.getOrDefault(roomId, Set.of()).size();
    }
}
