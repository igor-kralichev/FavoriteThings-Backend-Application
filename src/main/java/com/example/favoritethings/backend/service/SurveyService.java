package com.example.favoritethings.backend.service;

import com.example.favoritethings.backend.dto.SurveyDTO;
import com.example.favoritethings.backend.entity.Survey;
import com.example.favoritethings.backend.entity.User;
import com.example.favoritethings.backend.repository.SurveyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    private static final Logger logger = LoggerFactory.getLogger(SurveyService.class);

    @Autowired
    private SurveyRepository surveyRepository;

    @Transactional
    public void saveSurvey(SurveyDTO surveyDTO) {
        // Получаем текущего пользователя из SecurityContext
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = surveyRepository.findUserByEmail(username)
            .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + username));

        // Мапим SurveyDTO в сущность Survey
        Survey survey = new Survey();
        survey.setFavoriteFood(surveyDTO.getFavoriteFood());
        survey.setFavoriteColor(surveyDTO.getFavoriteColor());
        survey.setFavoriteSong(surveyDTO.getFavoriteSong());
        survey.setFavoriteDate(surveyDTO.getFavoriteDate());
        survey.setFavoriteNumber(surveyDTO.getFavoriteNumber());
        survey.setUser(user);

        // Сохраняем опрос
        surveyRepository.save(survey);
        logger.info("Опрос сохранён для пользователя: {}", username);
    }

    @Transactional(readOnly = true)
    public List<SurveyDTO> searchSurveys(String favoriteFood, String favoriteSong) {
        List<Survey> surveys = surveyRepository.findByFavoriteFoodAndFavoriteSong(favoriteFood, favoriteSong);
        return surveys.stream()
            .map(survey -> new SurveyDTO(
                survey.getId(),
                survey.getFavoriteFood(),
                survey.getFavoriteColor(),
                survey.getFavoriteSong(),
                survey.getFavoriteDate(),
                survey.getFavoriteNumber()
            ))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SurveyDTO> getUserSurveys() {
        // Получаем текущего пользователя из SecurityContext
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = surveyRepository.findUserByEmail(username)
            .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + username));

        return user.getSurveys().stream()
            .map(survey -> new SurveyDTO(
                survey.getId(),
                survey.getFavoriteFood(),
                survey.getFavoriteColor(),
                survey.getFavoriteSong(),
                survey.getFavoriteDate(),
                survey.getFavoriteNumber()
            ))
            .collect(Collectors.toList());
    }
}