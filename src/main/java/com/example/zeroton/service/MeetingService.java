package com.example.zeroton.service;

import com.example.zeroton.config.SecurityConfig;
import com.example.zeroton.dto.CustomUserDetails;
import com.example.zeroton.dto.MeetingSummaryDto;
import com.example.zeroton.entity.Meeting;
import com.example.zeroton.entity.Member;
import com.example.zeroton.entity.Message;
import com.example.zeroton.entity.Todo;
import com.example.zeroton.repository.MeetingRepository;
import com.example.zeroton.repository.MemberRepository;
import com.example.zeroton.repository.MessageRepository;
import com.example.zeroton.repository.TodoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final TodoRepository todoRepository;
    private final AiService aiService;
    private final SecurityConfig securityConfig;

    public String createMeeting(String title) {

        try {
            //현재 로그인한 사용자 정보 불러오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Member currentMember = memberRepository.findById(userDetails.getUsername()).get();

            //미팅 객체 만들기
            Meeting meeting = new Meeting(title);
            meeting.setHost(currentMember.getObjectId());
            meeting.setCode(UUID.randomUUID().toString());
            meetingRepository.save(meeting);
            joinMeeting(meeting.getCode());
            return meeting.getCode();
        } catch (Exception e) {

            e.printStackTrace();
            return "error";
        }


    }

    public String joinMeeting(String code) {
        //현재 로그인한 사용자 정보 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member currentMember = memberRepository.findById(userDetails.getUsername()).get();

        Optional<Meeting> found = meetingRepository.findByCode(code);

        if (found.isPresent()) {
            Meeting meeting = found.get();
            meeting.getParticipants().add(currentMember.getObjectId());
            meetingRepository.save(meeting);
            return "회의 참가 완료!";
        } else {
            return "코드를 다시 확인해주세요.";
        }
    }

    public String endMeeting(String code) throws JsonProcessingException {
        Optional<Meeting> found = meetingRepository.findByCode(code);
        if (found.isPresent()) {
            Meeting meeting = found.get();
            List<Message> foundMessages = messageRepository.findAllByMeetingIdOrderByCreatedAt(meeting.getCode());

            // StringBuilder를 이용해 "발화자: 내용" 형식의 문자열 생성
            StringBuilder transcript = new StringBuilder("{\n");

            for (Message msg : foundMessages) {
                transcript.append("  \"")
                        .append(msg.getSpeaker()) // 발화자
                        .append("\": \"")
                        .append(msg.getMessage()) // 내용
                        .append("\",\n");
            }

            // 마지막 쉼표 제거 및 JSON 형식 맞추기
            if (!foundMessages.isEmpty()) {
                transcript.setLength(transcript.length() - 2); // 마지막 `,\n` 제거
            }
            transcript.append("\n}");


            String aiResult = aiService.chat(transcript.toString());

            // Jackson ObjectMapper를 사용하여 JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(aiResult);
            // 1. summary 추출
            String summary = rootNode.get("summary").asText();
            meeting.setDescription(summary);
            meetingRepository.save(meeting);


            return aiResult;

        } else {
            return "코드를 다시 확인해주세요.";
        }
    }

    public List<Meeting> getMeetingsForMember() {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 현재 멤버의 objectId를 가져옴
        Member currentMember = memberRepository.findById(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Member not found"));
//        System.out.println(currentMember.getName());
        // currentMember.getObjectId()가 participants에 포함된 모든 회의 조회
        return meetingRepository.findAllByParticipantsContaining(currentMember.getObjectId());
    }


    public MeetingSummaryDto getMeetingsSummary(String code) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Member currentMember = memberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Meeting meeting = meetingRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        if (!meeting.getParticipants().contains(currentMember.getObjectId())) {
            throw new RuntimeException("User is not a participant of this meeting");
        }

        // ✅ 로그 추가: meetingId 확인
        System.out.println("Meeting ID: " + meeting.getObjectId());

        List<Todo> todos = todoRepository.findByMeetingId(meeting.getObjectId());

        // ✅ 로그 추가: 조회된 Todo 리스트 확인
        System.out.println("Todos found: " + todos.size());
        for (Todo todo : todos) {
            System.out.println("Todo: " + todo.getContent());
        }

        return new MeetingSummaryDto(meeting, todos);
    }

}

