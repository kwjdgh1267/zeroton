package com.example.zeroton.repository;

import com.example.zeroton.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findAllByMeetingIdOrderByCreatedAt(String meetingId);
}
