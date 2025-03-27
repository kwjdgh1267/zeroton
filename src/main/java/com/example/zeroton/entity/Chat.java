package com.example.zeroton.entity;

import com.example.zeroton.entity.Meeting;
import com.example.zeroton.entity.Message;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.util.List;

@Document(collection = "chat") // MongoDB 컬렉션명
@Getter
@Setter
public class Chat {
    @Id
    private String id;

    @DBRef
    private Meeting meeting; // 소속된 회의 (One-to-Squillions 관계)

    private List<Message> content; // 문장 단위 메시지 리스트

    // 생성자
    public Chat(Meeting meeting, List<Message> content) {
        this.meeting = meeting;
        this.content = content;
    }

    // 게터/세터
}
