package com.example.todo314.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.todo314.model.UserEntity;
import com.example.todo314.persistence.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity) {
        // 유저 정보 누락 항목 검사
        if (userEntity == null || userEntity.getEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }
        final String email = userEntity.getEmail();

        // 이메일 중복 검사
        if (userRepository.existsByEmail(email)) {
            log.warn("Email already exists {}", email);
            throw new RuntimeException("Email already exists");
        }

        return userRepository.save(userEntity);
    }

    public Optional<UserEntity> updateUser(final UserEntity entity) {

        if (userRepository.existsById(entity.getId())) {
            userRepository.save(entity);
        } else {
            throw new RuntimeException("Unknown id");
        }

        return userRepository.findById(entity.getId());
    }

    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        final UserEntity originalUser = userRepository.findByEmail(email);
        if (originalUser != null && encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        }
        return null;
    }

    public Optional<UserEntity> findById(String userId) {
        return userRepository.findById(userId);
    }
}
