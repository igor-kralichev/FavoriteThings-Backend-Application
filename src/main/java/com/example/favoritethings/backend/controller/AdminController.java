package com.example.favoritethings.backend.controller;

import com.example.favoritethings.backend.dto.UserDTO;
import com.example.favoritethings.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Администрирование", description = "Операции администратора (требуется роль ADMIN)")
public class AdminController {

    @Autowired
    private UserService userService;

    @Operation(
        summary = "Получить всех пользователей",
        description = "Возвращает список всех зарегистрированных пользователей c их результатами опросов",
        parameters = @Parameter(
            name = "Authorization",
            description = "JWT токен в формате Bearer <token>",
            required = true,
            in = ParameterIn.HEADER
        )
    )
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}