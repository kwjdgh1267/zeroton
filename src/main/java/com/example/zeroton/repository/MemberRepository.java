package com.example.zeroton.repository;

import com.example.zeroton.entity.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MemberRepository extends MongoRepository<Member, Long> {

    public Optional<Member> findById(String id);


}
