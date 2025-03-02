package com.example.favoritethings.backend.controller;

import com.example.favoritethings.backend.dto.ApiResponse;
import com.example.favoritethings.backend.dto.SurveyRequest;
import com.example.favoritethings.backend.entity.Survey;
import com.example.favoritethings.backend.service.SurveyService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для отправки данных опроса.
 * Доступен только для аутентифицированных пользователей.
 */
@RestController
@RequestMapping("/api/survey")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @PostMapping
    public ResponseEntity<ApiResponse> submitSurvey(@RequestBody SurveyRequest surveyRequest) {
        surveyService.saveSurvey(surveyRequest);
        return ResponseEntity.ok(new ApiResponse(true, "Опрос успешно отправлен."));
    }

    // Новый GET-эндпоинт для поиска опросов по параметрам
    @GetMapping("/search")
    public ResponseEntity<List<Survey>> searchSurveys(
            @RequestParam(required = false) String favoriteFood,
            @RequestParam(required = false) String favoriteSong) {
        List<Survey> surveys = surveyService.searchSurveys(favoriteFood, favoriteSong);
        return ResponseEntity.ok(surveys);
    }

    @GetMapping
public ResponseEntity<List<Survey>> getUserSurveys() {
    List<Survey> surveys = surveyService.getUserSurveys();
    return ResponseEntity.ok(surveys);
}

}
