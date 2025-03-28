package com.example.zeroton.controller;

import com.example.zeroton.entity.Chat;
import com.example.zeroton.service.AiService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final AiService gptService;

    public ChatController(AiService gptService) {
        this.gptService = gptService;
    }

    @PostMapping("/send-stream")
    public Flux<String> sendChatStream(@RequestBody Chat chat) {
        Flux<String> formattedMessages = Flux.fromIterable(chat.getContent())
                .map(msg -> msg.getSpeaker() + ": " + msg.getMessage());

        return gptService.sendChatStream(formattedMessages);
    }
}
