package com.example.favoritethings.backend.service;

import com.example.favoritethings.backend.dto.RegisterRequest;
import com.example.favoritethings.backend.entity.Role;
import com.example.favoritethings.backend.entity.User;
import com.example.favoritethings.backend.repository.RoleRepository;
import com.example.favoritethings.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * Сервис для работы с пользователями: регистрация, подтверждение email и получение списка пользователей.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // Простое хранилище токенов для подтверждения email (демонстрационный вариант)
    private Map<String, String> emailConfirmationTokens = new HashMap<>();

    @Transactional
    public void registerUser(RegisterRequest registerRequest) {
        // Здесь можно добавить дополнительную валидацию данных
        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setBirthDate(registerRequest.getBirthDate());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmailConfirmed(false);

        // Назначаем роль USER по умолчанию
        Role userRole = roleRepository.findByName("ROLE_USER")
            .orElseThrow(() -> new RuntimeException("Роль пользователя не найдена."));
        user.getRoles().add(userRole);

        userRepository.save(user);

        // Генерация токена подтверждения email
        String token = UUID.randomUUID().toString();
        emailConfirmationTokens.put(token, user.getEmail());

        // Отправка письма с подтверждением
        emailService.sendConfirmationEmail(user.getEmail(), token);
    }

    @Transactional
    public void confirmEmail(String token) {
        String email = emailConfirmationTokens.get(token);
        if (email == null) {
            throw new RuntimeException("Неверный токен подтверждения.");
        }
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Пользователь не найден."));
        user.setEmailConfirmed(true);
        userRepository.save(user);
        emailConfirmationTokens.remove(token);
    }

    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
