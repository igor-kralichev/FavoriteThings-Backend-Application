package com.example.favoritethings.backend.service;

import com.example.favoritethings.backend.dto.SurveyRequest;
import com.example.favoritethings.backend.entity.Survey;
import com.example.favoritethings.backend.entity.User;
import com.example.favoritethings.backend.repository.SurveyRepository;
import com.example.favoritethings.backend.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Сервис для обработки данных опроса.
 */
@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private UserRepository userRepository;

    public void saveSurvey(SurveyRequest surveyRequest) {
        // Получаем email текущего аутентифицированного пользователя
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Survey survey = new Survey();
        survey.setFavoriteFood(surveyRequest.getFavoriteFood());
        survey.setFavoriteColor(surveyRequest.getFavoriteColor());
        survey.setFavoriteSong(surveyRequest.getFavoriteSong());
        survey.setFavoriteDate(surveyRequest.getFavoriteDate());
        survey.setFavoriteNumber(surveyRequest.getFavoriteNumber());
        survey.setUser(user);

        surveyRepository.save(survey);
    }

    // Новый метод для поиска опросов по параметрам
    public List<Survey> searchSurveys(String favoriteFood, String favoriteSong) {
        if (favoriteFood != null && !favoriteFood.isEmpty() && favoriteSong != null && !favoriteSong.isEmpty()) {
            return surveyRepository.findByFavoriteFoodContainingIgnoreCaseAndFavoriteSongContainingIgnoreCase(favoriteFood, favoriteSong);
        } else if (favoriteFood != null && !favoriteFood.isEmpty()) {
            return surveyRepository.findByFavoriteFoodContainingIgnoreCase(favoriteFood);
        } else if (favoriteSong != null && !favoriteSong.isEmpty()) {
            return surveyRepository.findByFavoriteSongContainingIgnoreCase(favoriteSong);
        } else {
            // Если параметры не заданы, возвращаем все опросы
            return surveyRepository.findAll();
        }
    }

    public List<Survey> getUserSurveys() {
        // Получаем email текущего аутентифицированного пользователя
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    
        // Получаем все опросы, связанные с этим пользователем
        return surveyRepository.findByUser(user);
    }
    
}
