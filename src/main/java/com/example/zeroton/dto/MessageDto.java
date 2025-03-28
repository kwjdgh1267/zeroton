package com.example.zeroton.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class MessageDto {
    private String speaker; // 말한 사람 (사용자 ID 또는 이름)
    private String message; // 메시지 내용
}
