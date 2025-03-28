package com.example.zeroton.controller;

import com.example.zeroton.entity.Meeting;
import com.example.zeroton.service.MeetingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http:localhost:3000")
@RestController
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;



    @GetMapping("/meeting")
    public Optional<Meeting> getUserMeetings() {
        // 사용자가 소속된 회의 목록을 서비스에서 가져옴
        return meetingService.getMeetingsForMember();
    }
    //회의실 생성
    @PostMapping("/meeting")
    public String createMeeting(@RequestParam String title){
        return meetingService.createMeeting(title);
    }

    //회의실 참가
    @PostMapping("/meeting/join")
    public String joinMeeting(@RequestParam String code){
        return meetingService.joinMeeting(code);
    }

    @PostMapping("/meeting/end")
    public String endMeeting(@RequestParam String code) throws JsonProcessingException {
        return meetingService.endMeeting(code);
    }


}
