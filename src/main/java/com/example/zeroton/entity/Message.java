package com.example.zeroton.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class Message {
    private String speaker; // 말한 사람 (사용자 ID 또는 이름)
    private String message; // 메시지 내용
    private LocalDateTime timestamp; // 보낸 시간

    // 생성자
    public Message(String speaker, String message, LocalDateTime timestamp) {
        this.speaker = speaker;
        this.message = message;
        this.timestamp = timestamp;
    }


}
