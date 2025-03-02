package com.example.favoritethings.backend.repository;

import com.example.favoritethings.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью User.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
