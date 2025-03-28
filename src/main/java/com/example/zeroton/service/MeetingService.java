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

    public String createMeeting(String title){

        try{
            //현재 로그인한 사용자 정보 불러오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Member currentMember = memberRepository.findById(userDetails.getUsername()).get();

            //미팅 객체 만들기
            Meeting meeting = new Meeting(title);
            meeting.setHost(currentMember.getName());
            meeting.setCode(UUID.randomUUID().toString());
            meetingRepository.save(meeting);
            joinMeeting(meeting.getCode());
            return meeting.getCode();
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
            meeting.getParticipants().add(currentMember.getName());
            meetingRepository.save(meeting);
            return "회의 참가 완료!";
        }else{
            return "코드를 다시 확인해주세요.";
        }
    }

    public String endMeeting(String code) throws JsonProcessingException {
        Optional<Meeting> found = meetingRepository.findByCode(code);
        if(found.isPresent()){
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


            // 2. todo 목록 추출
            JsonNode todoNode = rootNode.get("todo");
            if (todoNode != null && todoNode.isObject()) {
                List<Todo> todos = new ArrayList<>();

                Iterator<Map.Entry<String, JsonNode>> fields = todoNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> field = fields.next();
                    String asignee = field.getKey();
                    String content = field.getValue().asText();

                    // Todo 객체 생성
                    Todo todo = new Todo();
                    todo.setMeetingId(meeting.getObjectId());  // 미팅 ID 설정
                    todo.setAsignee(asignee);  // 할당자 설정
                    todo.setContent(content);  // 할 일 내용 설정
                    todo.setStatus(false);  // 기본 상태는 미완료로 설정

                    todos.add(todo);
                }

                // 할 일 목록 저장
                todoRepository.saveAll(todos);
            }




            return aiResult;

        }else{
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
        return meetingRepository.findAllByParticipantsContaining(currentMember.getName());
    }


    public MeetingSummaryDto getMeetingsSummary(String code) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 현재 멤버의 objectId를 가져옴

        Optional<Member> currentMember = memberRepository.findById(userDetails.getUsername());
        if(!currentMember.isPresent()){
            log.info("에러 발생23123213213123123");
            return null;
        }


        // 코드로 회의 찾기
        Meeting meeting = meetingRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        // 사용자가 해당 회의의 참가자인지 확인
        if (!meeting.getParticipants().contains(currentMember.get().getName())) {
            throw new RuntimeException("User is not a participant of this meeting");
        }

        // 해당 회의의 todo 리스트 가져오기
        List<Todo> todos = todoRepository.findByMeetingId(meeting.getObjectId());//여기

        log.info("todos: "+todos.size());

        // 결과 반환
        MeetingSummaryDto meetingSummaryDto = new MeetingSummaryDto(meeting, todos);

        // 참가자 목록을 Member로부터 이름만 가져오기
        List<String> participantNames = new ArrayList<>();
        for (String participantName : meeting.getParticipants()) {
            // ObjectId로 변환하여 Member 조회
            Optional<Member> participant = memberRepository.findByName(participantName); // 여기에 ObjectId로 변환 시도

            if (participant.isPresent()) {
                System.out.println(participant.get().getName());
                participantNames.add(participant.get().getName());  // 참가자 이름을 리스트에 추가
            } else {

                participantNames.add("Unknown");
            }
        }

        meetingSummaryDto.setParticipants(participantNames);
        Optional<Member> host = memberRepository.findByName(meetingSummaryDto.getHost());// 참가자 이름을 추가
        meetingSummaryDto.setHost(host.get().getName());  // 참가자 이름을 추가

        return meetingSummaryDto;
    }

    public void DeleteAllMeeting() {
        memberRepository.deleteAll();
    }
}

