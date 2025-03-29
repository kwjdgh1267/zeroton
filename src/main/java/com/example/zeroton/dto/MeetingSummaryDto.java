package com.example.zeroton.dto;

import com.example.zeroton.dto.TodoDto;
import com.example.zeroton.entity.Meeting;
import com.example.zeroton.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingSummaryDto {
    private String objectId;
    private String title;
    private String code;
    private String host;
    private List<String> participants;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TodoDto> todos;

    public MeetingSummaryDto(Meeting meeting, List<Todo> todos) {
        this.objectId = meeting.getObjectId();
        this.title = meeting.getTitle();
        this.code = meeting.getCode();
        this.host = meeting.getHost();
        this.participants = meeting.getParticipants();
        this.description = meeting.getDescription();
        this.createdAt = meeting.getCreatedAt();
        this.updatedAt = meeting.getUpdatedAt();

        // ✅ todos 변환 (리스트 전체 변환 메서드 사용)
        this.todos = todos != null ? TodoDto.fromEntityList(todos) : new ArrayList<>();
    }
}