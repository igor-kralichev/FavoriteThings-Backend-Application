package com.example.favoritethings.backend.controller;

import com.example.favoritethings.backend.dto.ApiResponseDTO;
import com.example.favoritethings.backend.dto.LoginDTO;
import com.example.favoritethings.backend.dto.RegisterDTO;
import com.example.favoritethings.backend.security.CustomUserDetailsService;
import com.example.favoritethings.backend.security.JwtTokenProvider;
import com.example.favoritethings.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Аутентификация", description = "Регистрация, подтверждение email, логин/логаут")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService; // Добавляем для загрузки ролей

    public AuthController(UserService userService,
                          JwtTokenProvider jwtTokenProvider,
                          AuthenticationManager authenticationManager,
                          CustomUserDetailsService userDetailsService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @Operation(summary = "Регистрация нового пользователя")
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO> register(@Valid @RequestBody RegisterDTO request) {
        try {
            userService.registerUser(request);
            logger.info("Пользователь зарегистрирован: {}", request.getEmail());
            return ResponseEntity.ok(
                new ApiResponseDTO(true, "Пользователь зарегистрирован. Проверьте email для подтверждения.")
            );
        } catch (Exception e) {
            logger.error("Ошибка при регистрации пользователя {}: {}", request.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "Ошибка при регистрации: " + e.getMessage()));
        }
    }

    @Operation(summary = "Подтверждение email по токену")
    @GetMapping("/confirm")
    public ResponseEntity<ApiResponseDTO> confirmEmail(@RequestParam String token) {
        try {
            userService.confirmEmail(token);
            logger.info("Email подтверждён с токеном: {}", token);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Email подтверждён"));
        } catch (Exception e) {
            logger.error("Ошибка при подтверждении email с токеном {}: {}", token, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    @Operation(summary = "Логин и выдача JWT токенов")
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO> login(@Valid @RequestBody LoginDTO request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            Map<String, String> tokens = jwtTokenProvider.generateTokens(authentication);

            Cookie refreshCookie = new Cookie("refresh_token", tokens.get("refresh_token"));
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(false); // Установите true в продакшене
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 дней
            response.addCookie(refreshCookie);

            logger.info("Пользователь вошёл в систему: {}", request.getEmail());
            return ResponseEntity.ok(
                new ApiResponseDTO(true, Map.of("access_token", tokens.get("access_token")))
            );
        } catch (BadCredentialsException e) {
            logger.warn("Неудачная попытка входа для email: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(new ApiResponseDTO(false, "Неверный email или пароль"));
        }
    }

    @Operation(
        summary = "Обновление access_token по refresh_token из куки",
        description = "Берёт refresh_token из HttpOnly cookie и выдаёт новый access_token"
    )
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDTO> refreshToken(HttpServletRequest request) {
        // Извлекаем refresh_token из куки
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        // Проверяем, есть ли refresh_token и валиден ли он
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            String username = jwtTokenProvider.getUsernameFromToken(refreshToken);

            // Загружаем пользователя, чтобы получить его роль
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities()
            );

            // Генерируем новый access_token с ролью
            Map<String, String> tokens = jwtTokenProvider.generateTokens(authentication);
            String newAccessToken = tokens.get("access_token");

            logger.info("Access token обновлён для пользователя: {}", username);
            return ResponseEntity.ok(new ApiResponseDTO(true, Map.of("access_token", newAccessToken)));
        } else {
            logger.warn("Недействительный или отсутствующий refresh_token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiResponseDTO(false, "Недействительный или отсутствующий refresh_token")
            );
        }
    }

    @Operation(summary = "Логаут (удаление refresh_token cookie)")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO> logout(HttpServletResponse response) {
        Cookie refreshCookie = new Cookie("refresh_token", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false); // Установите true в продакшене
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);

        logger.info("Пользователь вышел из системы");
        return ResponseEntity.ok(new ApiResponseDTO(true, "Выход выполнен"));
    }
}