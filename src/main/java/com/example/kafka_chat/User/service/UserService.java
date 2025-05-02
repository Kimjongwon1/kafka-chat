package com.example.kafka_chat.User.service;

import com.example.kafka_chat.User.entity.User;

public interface UserService {
    User authenticate(String username, String password);
    void register(String username, String password);

}
