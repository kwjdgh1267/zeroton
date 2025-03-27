package com.example.zeroton.service;

import com.example.zeroton.dto.CustomUserDetails;
import com.example.zeroton.entity.Meeting;
import com.example.zeroton.entity.Member;
import com.example.zeroton.repository.MeetingRepository;
import com.example.zeroton.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;

    public String createMeeting(String title){

        try{
            //현재 로그인한 사용자 정보 불러오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Member currentMember = memberRepository.findById(userDetails.getUsername()).get();

            //미팅 객체 만들기
            Meeting meeting = new Meeting(title);
            meeting.setHost(currentMember.getObjectId());
            meeting.setCode(UUID.randomUUID().toString());
            meetingRepository.save(meeting);
            return "회의 생성 완료! code: "+meeting.getCode();
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }


    }

    public String joinMeeting(String code){
        //현재 로그인한 사용자 정보 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member currentMember = memberRepository.findById(userDetails.getUsername()).get();

        Optional<Meeting> found = meetingRepository.findByCode(code);

        if(found.isPresent()){
            Meeting meeting = found.get();
            meeting.getParticipants().add(currentMember.getObjectId());
            meetingRepository.save(meeting);
            return "회의 참가 완료!";
        }else{
            return "코드를 다시 확인해주세요.";
        }
    }

}
