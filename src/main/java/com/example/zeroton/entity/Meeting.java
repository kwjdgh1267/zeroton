package com.example.zeroton.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "meetings")
@Getter
@Setter
@NoArgsConstructor
public class Meeting {
    @Id
    private String objectId;

    private String title;

    private String code;

    private String host;

    private List<String> participants;

    private String description;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Meeting(String title) {
        this.title = title;
        participants = new ArrayList<>();
    }
}
