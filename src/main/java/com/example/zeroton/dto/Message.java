package com.example.zeroton.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {

    private String role;
    private String content;

    // constructor, getters and setters
}