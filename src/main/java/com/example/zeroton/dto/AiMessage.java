package com.example.zeroton.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AiMessage {

    private String role;
    private String content;

    // constructor, getters and setters
}