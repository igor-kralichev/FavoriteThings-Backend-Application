package com.example.favoritethings.backend.security;

import com.example.favoritethings.backend.entity.User;
import com.example.favoritethings.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import java.util.List;

/**
 * Сервис для загрузки данных пользователя из базы.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден с email: " + email));

        // Проверка подтверждения email
        if (!user.isEmailConfirmed()) {
            throw new UsernameNotFoundException("Email не подтверждён для пользователя: " + email);
        }

        // Получаем единственную роль (может быть null)
        if (user.getRole() != null) {
            List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(user.getRole().getName())
            );
            return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
            );
        } else {
            // Если роли нет, возвращаем без прав
            return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
            );
        }
    }
}
