package com.example.todo314.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo314.model.TodoEntity;
import com.example.todo314.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository repository;

    public List<TodoEntity> create(final TodoEntity entity) {
        // Validations
        validate(entity);
        repository.save(entity);
        return repository.findByUserId(entity.getUserId());
    }

    public List<TodoEntity> retrieve(final String userId) {
        return repository.findByUserId(userId);
    }

    public List<TodoEntity> updateTodo(final TodoEntity entity) {
        // Validations
        validate(entity);

        if (repository.existsById(entity.getId())) {
            repository.save(entity);
        } else {
            throw new RuntimeException("Unknown id");
        }

        return repository.findByUserId(entity.getUserId());
    }

    public List<TodoEntity> delete(final TodoEntity entity) {
        if (repository.existsById(entity.getId())) {
            repository.deleteById(entity.getId());
        } else {
            throw new RuntimeException("id does not exist");
        }

        return repository.findByUserId(entity.getUserId());
    }

    public void validate(final TodoEntity entity) {
        if (entity == null) {
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }
        if (entity.getUserId() == null) {
            log.warn("Unknown User");
            throw new RuntimeException("Unknown User");
        }
    }
}
