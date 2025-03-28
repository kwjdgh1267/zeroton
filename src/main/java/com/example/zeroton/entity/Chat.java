package com.example.zeroton.entity;

import com.example.zeroton.dto.MessageDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Data
@Getter
@Setter
public class Chat {
    private String meetingId;
    private List<MessageDto> content; // 문장 단위 메시지 리스트
}
