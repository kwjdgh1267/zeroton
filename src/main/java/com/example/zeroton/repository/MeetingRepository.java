package com.example.zeroton.repository;

import com.example.zeroton.entity.Meeting;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends MongoRepository<Meeting, String> {
    Optional<Meeting> findByCode(String code);
    // participants 리스트에 member의 objectId가 포함된 모든 회의를 찾는 메서드
    List<Meeting> findAllByParticipantsContaining(String name);
}
