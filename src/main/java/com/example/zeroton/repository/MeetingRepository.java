package com.example.zeroton.repository;

import com.example.zeroton.entity.Meeting;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MeetingRepository extends MongoRepository<Meeting, String> {
    Optional<Meeting> findByCode(String code);
}
