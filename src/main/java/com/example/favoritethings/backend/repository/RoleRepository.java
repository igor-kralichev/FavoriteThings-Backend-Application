package com.example.favoritethings.backend.repository;

import com.example.favoritethings.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью Role.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
