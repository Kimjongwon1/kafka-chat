package com.example.kafka_chat.User.controller;

import com.example.kafka_chat.User.entity.User;
import com.example.kafka_chat.User.request.LoginRequest;
import com.example.kafka_chat.User.response.LoginResponse;
import com.example.kafka_chat.User.service.UserService;
import com.example.kafka_chat.User.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.authenticate(loginRequest.username(), loginRequest.password());
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(Map.of("message", "로그인 성공", "role", user.getRole(),"username", user.getUsername() ,"id",user.getId()));

    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest request) {
        userService.register(request.username(), request.password());
        return ResponseEntity.ok("회원가입 성공");
    }

}
