package com.example.zeroton.controller;

import com.example.zeroton.service.MeetingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

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
