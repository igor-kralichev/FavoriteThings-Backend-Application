package com.example.favoritethings.backend.controller;

import com.example.favoritethings.backend.dto.ApiResponseDTO;
import com.example.favoritethings.backend.dto.SurveyDTO;
import com.example.favoritethings.backend.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/survey")
@Tag(name = "Опросы", description = "Работа с пользовательскими опросами")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @Operation(
        summary = "Отправить опрос",
        description = "Сохраняет ответы опроса от текущего пользователя",
        parameters = @Parameter(
            name = "Authorization",
            description = "JWT токен в формате Bearer <token>",
            required = true,
            in = ParameterIn.HEADER
        )
    )
    @PostMapping
    public ResponseEntity<ApiResponseDTO> submitSurvey(@RequestBody SurveyDTO surveyDTO) {
        surveyService.saveSurvey(surveyDTO);
        return ResponseEntity.ok(new ApiResponseDTO(true, "Опрос успешно отправлен."));
    }


    @Operation(
        summary = "Получить свои опросы",
        parameters = @Parameter(
            name = "Authorization",
            description = "JWT токен в формате Bearer <token>",
            required = true,
            in = ParameterIn.HEADER
        )
    )
    @GetMapping
    public ResponseEntity<List<SurveyDTO>> getUserSurveys() {
        List<SurveyDTO> surveys = surveyService.getUserSurveys();
        return ResponseEntity.ok(surveys);
    }
}