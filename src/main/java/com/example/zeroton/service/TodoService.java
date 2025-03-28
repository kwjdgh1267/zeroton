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
    public String updateStatus(String id) {

        Optional<Todo> found = todoRepository.findById(id);
        if (found.isPresent()) {
            Todo todo = found.get();
            todo.setStatus(!(todo.isStatus()));
            return "status : " + todo.isStatus();
        }
        else{
            return "Todo를 불러오는 중 오류가 발생했습니다";
        }

    }


    public void deleteTodo(String id) {
        todoRepository.deleteById(id);
    }
}