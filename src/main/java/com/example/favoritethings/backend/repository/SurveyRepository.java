package com.example.favoritethings.backend.repository;

import com.example.favoritethings.backend.entity.Survey;
import com.example.favoritethings.backend.entity.User;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью Survey.
 */
public interface SurveyRepository extends JpaRepository<Survey, Long> {
   // Поиск по любимому блюду с частичным совпадением
   List<Survey> findByFavoriteFoodContainingIgnoreCase(String favoriteFood);
    
   // Поиск по любимой песне с частичным совпадением
   List<Survey> findByFavoriteSongContainingIgnoreCase(String favoriteSong);
   
   // Поиск одновременно по любимому блюду и песне
   List<Survey> findByFavoriteFoodContainingIgnoreCaseAndFavoriteSongContainingIgnoreCase(String favoriteFood, String favoriteSong);

   // Поиск по пользователю
   List<Survey> findByUser(User user);
}
