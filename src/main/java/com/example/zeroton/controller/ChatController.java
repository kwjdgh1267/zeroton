package com.example.zeroton.controller;

import com.example.zeroton.dto.MessageDto;
import com.example.zeroton.entity.Chat;
import com.example.zeroton.entity.Message;
import com.example.zeroton.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http:localhost:3000")
@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final MessageRepository messageRepository;

    @PostMapping("/make-data")
    public String makeData(@RequestBody Chat chat) {
        String meetingId=chat.getMeetingId();//코드

        for (MessageDto message : chat.getContent()) {
            log.info(meetingId+"-저장: "+message.getSpeaker()+ ": "+message.getMessage());
            messageRepository.save(new Message(meetingId,message.getSpeaker(), message.getMessage()));
        }
        return "데이터 저장 완료";
    }

}
