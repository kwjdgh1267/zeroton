package com.example.zeroton.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AiRequest {

    private String model;
    private List<Message> messages;

    public AiRequest(String model) {
        this.model = model;

        this.messages = new ArrayList<>();
    }

    // getters and setters
}