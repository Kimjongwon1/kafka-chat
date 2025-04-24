package com.example.kafka_chat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.kafka_chat.chat") // 정확한 패키지
public class KafkaChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaChatApplication.class, args);
	}

}
