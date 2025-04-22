package com.example.favoritethings.backend.service;

import com.example.favoritethings.backend.dto.RegisterDTO;
import com.example.favoritethings.backend.dto.SurveyDTO;
import com.example.favoritethings.backend.dto.UserDTO;
import com.example.favoritethings.backend.entity.ConfirmationToken;
import com.example.favoritethings.backend.entity.Role;
import com.example.favoritethings.backend.entity.User;
import com.example.favoritethings.backend.repository.ConfirmationTokenRepository;
import com.example.favoritethings.backend.repository.RoleRepository;
import com.example.favoritethings.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ConfirmationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       ConfirmationTokenRepository tokenRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public void registerUser(RegisterDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Пользователь с email " + request.getEmail() + " уже существует");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
            .orElseThrow(() -> {
                logger.error("Роль ROLE_USER не найдена в базе данных");
                return new RuntimeException("Роль пользователя не найдена.");
            });

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setBirthDate(request.getBirthDate());
        user.setEmailConfirmed(false);
        user.setRole(userRole);

        userRepository.save(user);
        logger.info("Пользователь успешно зарегистрирован: {}", request.getEmail());

        // Генерируем токен подтверждения
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15), // Токен действителен 15 минут
            user
        );
        tokenRepository.save(confirmationToken);

        // Отправляем письмо
        emailService.sendConfirmationEmail(user.getEmail(), token);
    }

    public void confirmEmail(String token) {
        ConfirmationToken confirmationToken = tokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Токен не найден"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new RuntimeException("Email уже подтверждён");
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Токен истёк");
        }

        User user = confirmationToken.getUser();
        user.setEmailConfirmed(true);
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        userRepository.save(user);
        tokenRepository.save(confirmationToken);

        logger.info("Email подтверждён для пользователя: {}", user.getEmail());
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
            .map(user -> {
                Object surveysData = user.getSurveys().isEmpty()
                    ? "нет пройденных опросов"
                    : user.getSurveys().stream()
                        .map(survey -> new SurveyDTO(
                            survey.getId(),
                            survey.getFavoriteFood(),
                            survey.getFavoriteColor(),
                            survey.getFavoriteSong(),
                            survey.getFavoriteDate(),
                            survey.getFavoriteNumber()
                        ))
                        .collect(Collectors.toList());

                return new UserDTO(
                    user.getId(),
                    user.getFullName(),
                    user.getBirthDate(),
                    user.getEmail(),
                    user.isEmailConfirmed(),
                    user.getRole() != null ? user.getRole().getName() : null,
                    surveysData
                );
            })
            .collect(Collectors.toList());
    }
}