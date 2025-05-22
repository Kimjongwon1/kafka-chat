package com.example.kafka_chat.User.impl;

import com.example.kafka_chat.User.entity.User;
import com.example.kafka_chat.User.repository.UserRepository;
import com.example.kafka_chat.User.request.LoginRequest;
import com.example.kafka_chat.User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return user;
    }

    @Override
    public void register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role("ROLE_USER")
                .build();

        userRepository.save(user);
    }

}
