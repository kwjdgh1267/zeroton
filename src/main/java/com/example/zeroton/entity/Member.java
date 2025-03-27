package com.example.zeroton.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "members")
@Getter
@Setter
@NoArgsConstructor
public class Member{
    @Id
    private String objectId;

    private String id;

    private String password;

    private String name;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Member(String id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name= name;
    }
}
