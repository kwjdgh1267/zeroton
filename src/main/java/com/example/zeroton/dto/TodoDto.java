package com.example.zeroton.dto;

import com.example.zeroton.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoDto {
    private String objectId;
    private String meetingId;
    private String content;
    private String asignee;
    private boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TodoDto(Todo todo) {
    }

    public static TodoDto fromEntity(Todo todo) {
        return new TodoDto(
                todo.getObjectId(),
                todo.getMeetingId(),
                todo.getContent(),
                todo.getAsignee(),
                todo.isStatus(),
                todo.getCreatedAt(),
                todo.getUpdatedAt()
        );
    }

    public static List<TodoDto> fromEntityList(List<Todo> todos) {
        return todos.stream().map(TodoDto::fromEntity).collect(Collectors.toList());
    }



    public Todo toEntity() {
        Todo todo = new Todo();
        todo.setObjectId(this.objectId);
        todo.setMeetingId(this.meetingId);
        todo.setContent(this.content);
        todo.setAsignee(this.asignee);
        todo.setStatus(this.status);
        todo.setCreatedAt(this.createdAt);
        todo.setUpdatedAt(this.updatedAt);
        return todo;
    }
}