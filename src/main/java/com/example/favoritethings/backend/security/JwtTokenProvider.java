package com.example.favoritethings.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 1800000; // 30 минут
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 604800000; // 7 дней

    // Генерация токенов (access и refresh)
    public Map<String, String> generateTokens(Authentication authentication) {
        String username = authentication.getName();

        // Получаем единственную роль
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null); // Если роли нет, будет null

        // Создаём access-токен
        String accessToken = Jwts.builder()
                .setSubject(username)
                .claim("role", role) // Записываем одну роль
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(key)
                .compact();

        // Создаём refresh-токен
        String refreshToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(key)
                .compact();

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return tokens;
    }

    // Метод для получения имени пользователя из токена
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Метод для получения роли пользователя из токена
    public List<GrantedAuthority> getRolesFromToken(String token) {
        String role = getClaimFromToken(token, claims -> claims.get("role", String.class));
        if (role != null && !role.isEmpty()) {
            return List.of(new SimpleGrantedAuthority(role));
        }
        return List.of(); // Если роли нет, возвращаем пустой список
    }

    // Валидация токена
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Извлечение одного клейма (общий метод)
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    // Getter для ключа
    public Key getKey() {
        return key;
    }
}