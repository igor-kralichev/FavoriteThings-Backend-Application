package com.example.favoritethings.backend.controller;

import com.example.favoritethings.backend.dto.ApiResponse;
import com.example.favoritethings.backend.dto.LoginRequest;
import com.example.favoritethings.backend.dto.RegisterRequest;
import com.example.favoritethings.backend.security.JwtTokenProvider;
import com.example.favoritethings.backend.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager; // Если потребуется, можно определить bean для AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для регистрации, подтверждения email и логина.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, 
                         JwtTokenProvider jwtTokenProvider,
                         AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok(
            new ApiResponse(true, "Пользователь зарегистрирован. Проверьте email для подтверждения.")
        );
    }

    @GetMapping("/confirm")
    public ResponseEntity<ApiResponse> confirmEmail(@RequestParam String token) {
        userService.confirmEmail(token);
        return ResponseEntity.ok(
            new ApiResponse(true, "Email подтверждён")
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            String jwt = jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new ApiResponse(true, jwt));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiResponse(false, "Неверный email или пароль")
            );
        }
    }
}
