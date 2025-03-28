package com.example.zeroton.controller;

import com.example.zeroton.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@Slf4j
public class AiController {
    private final AiService aiService;


    //ai에게 요청보낼때 쓰는거. 나중에 이름이랑 경로 수정 예정임
    @PostMapping("/chat")
    public String chat(@RequestBody Map<String, String> data) {
        return aiService.chat(data.get("prompt"));
    }


}