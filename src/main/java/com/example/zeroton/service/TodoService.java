package com.example.zeroton.service;

import com.example.zeroton.dto.TodoDto;
import com.example.zeroton.entity.Todo;
import com.example.zeroton.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoDto> getAllTodos() {
        return todoRepository.findAll().stream()
                .map(TodoDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<TodoDto> getTodoById(String id) {
        return todoRepository.findById(id).map(TodoDto::fromEntity);
    }

    public TodoDto createTodo(TodoDto todoDto) {
        Todo todo = todoDto.toEntity();
        return TodoDto.fromEntity(todoRepository.save(todo));
    }

    public Optional<TodoDto> updateTodo(String id, TodoDto updatedTodoDto) {
        return todoRepository.findById(id).map(todo -> {
            todo.setContent(updatedTodoDto.getContent());
            todo.setAsignee(updatedTodoDto.getAsignee());
            todo.setStatus(updatedTodoDto.isStatus());
            return TodoDto.fromEntity(todoRepository.save(todo));
        });
    }

    public void deleteTodo(String id) {
        todoRepository.deleteById(id);
    }
}