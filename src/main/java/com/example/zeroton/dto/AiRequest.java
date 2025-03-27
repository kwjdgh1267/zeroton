package com.example.zeroton.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AiRequest {

    private String model;
    private List<AiMessage> aiMessages;

    public AiRequest(String model) {
        this.model = model;

        this.aiMessages = new ArrayList<>();
    }

    // getters and setters
}