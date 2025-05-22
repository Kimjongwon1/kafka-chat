package com.example.kafka_chat.User.service;

import com.example.kafka_chat.User.entity.User;
import com.example.kafka_chat.User.request.LoginRequest;

public interface UserService {
    User authenticate(String username, String password);
    void register(String username, String password);

}
