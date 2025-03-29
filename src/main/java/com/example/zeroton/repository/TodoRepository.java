package com.example.zeroton.repository;

import com.example.zeroton.entity.Member;
import com.example.zeroton.entity.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends MongoRepository<Todo, String> {
    List<Todo> findByMeetingId(String meetingId);


}
