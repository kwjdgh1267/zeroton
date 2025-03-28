package com.example.zeroton.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "message")
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    private String objectId;

    private String meetingId;
    private String speaker; // 말한 사람 (사용자 ID 또는 이름)
    private String message; // 메시지 내용
    private LocalDateTime timestamp; // 보낸 시간

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Message(String meetingId, String speaker, String message) {
        this.meetingId = meetingId;
        this.speaker = speaker;
        this.message = message;
    }
}
