package com.example.favoritethings.backend.repository;

import com.example.favoritethings.backend.entity.Survey;
import com.example.favoritethings.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

    // Поиск опросов по favoriteFood и favoriteSong (игнорируя null параметры)
    @Query("SELECT s FROM Survey s WHERE (:favoriteFood IS NULL OR s.favoriteFood = :favoriteFood) " +
           "AND (:favoriteSong IS NULL OR s.favoriteSong = :favoriteSong)")
    List<Survey> findByFavoriteFoodAndFavoriteSong(
        @Param("favoriteFood") String favoriteFood,
        @Param("favoriteSong") String favoriteSong
    );

    // Поиск пользователя по email
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findUserByEmail(@Param("email") String email);
}