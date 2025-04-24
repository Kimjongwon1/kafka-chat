package com.example.kafka_chat.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMapper chatMapper;

    public List<ChatMessage> getMessageHistory() {
        return chatMapper.getAllMessages();
    }
}
