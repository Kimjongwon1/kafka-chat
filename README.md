## ⚙️ 환경설정
- `application.yml`은 Git에 포함되어 있지 않습니다.
- 아래 명령어로 예시 설정 복사 후 값 채우세요.

```bash
cp src/main/resources/application-example.yml src/main/resources/application.yml

server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/chatdb?characterEncoding=utf8&useUnicode=true
    username: your_db_user
    password: your_db_pass
    driver-class-name: com.mysql.cj.jdbc.Driver

  kafka:
    bootstrap-servers: localhost:9092

mybatis:
  mapper-locations: classpath:mappers/**/*.xml
  type-aliases-package: com.example.kafka_chat.chat
