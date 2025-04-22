package com.example.favoritethings.backend.config;

import com.example.favoritethings.backend.entity.Role;
import com.example.favoritethings.backend.entity.Survey;
import com.example.favoritethings.backend.entity.User;
import com.example.favoritethings.backend.repository.RoleRepository;
import com.example.favoritethings.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Role userRole = roleRepository.findByName("ROLE_USER")
            .orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName("ROLE_USER");
                roleRepository.save(newRole);
                logger.info("Создана роль: ROLE_USER");
                return newRole;
            });

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
            .orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName("ROLE_ADMIN");
                roleRepository.save(newRole);
                logger.info("Создана роль: ROLE_ADMIN");
                return newRole;
            });

        Optional<User> adminOpt = userRepository.findByEmail("admin@example.com");
        if (adminOpt.isEmpty()) {
            User admin = new User();
            admin.setFullName("Администратор");
            admin.setBirthDate(LocalDate.of(1970, 1, 1));
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmailConfirmed(true);
            admin.setRole(adminRole);

            Survey survey1 = new Survey();
            survey1.setFavoriteFood("Пицца");
            survey1.setFavoriteColor("#FF0000");
            survey1.setFavoriteSong("Bohemian Rhapsody");
            survey1.setFavoriteDate(LocalDate.of(2020, 1, 1));
            survey1.setFavoriteNumber(42);
            survey1.setUser(admin);

            Survey survey2 = new Survey();
            survey2.setFavoriteFood("Суши");
            survey2.setFavoriteColor("#00FF00");
            survey2.setFavoriteSong("Sweet Child O' Mine");
            survey2.setFavoriteDate(LocalDate.of(2021, 2, 2));
            survey2.setFavoriteNumber(7);
            survey2.setUser(admin);

            admin.getSurveys().add(survey1);
            admin.getSurveys().add(survey2);

            userRepository.save(admin);
            logger.info("Администратор создан: admin@example.com / admin123");
        } else {
            logger.info("Администратор уже есть в базе данных");
        }
    }
}