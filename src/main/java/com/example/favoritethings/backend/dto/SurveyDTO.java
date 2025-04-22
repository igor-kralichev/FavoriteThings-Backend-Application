package com.example.favoritethings.backend.dto;

import java.time.LocalDate;

/**
 * DTO для представления опроса в ответе.
 */
public class SurveyDTO {

    private Long id;
    private String favoriteFood;
    private String favoriteColor; // HEX значение (например, "#FFFFFF")
    private String favoriteSong;
    private LocalDate favoriteDate;
    private Integer favoriteNumber;

    // Конструкторы
    public SurveyDTO() {}

    public SurveyDTO(Long id, String favoriteFood, String favoriteColor, String favoriteSong, LocalDate favoriteDate, Integer favoriteNumber) {
        this.id = id;
        this.favoriteFood = favoriteFood;
        this.favoriteColor = favoriteColor;
        this.favoriteSong = favoriteSong;
        this.favoriteDate = favoriteDate;
        this.favoriteNumber = favoriteNumber;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFavoriteFood() { return favoriteFood; }
    public void setFavoriteFood(String favoriteFood) { this.favoriteFood = favoriteFood; }

    public String getFavoriteColor() { return favoriteColor; }
    public void setFavoriteColor(String favoriteColor) { this.favoriteColor = favoriteColor; }

    public String getFavoriteSong() { return favoriteSong; }
    public void setFavoriteSong(String favoriteSong) { this.favoriteSong = favoriteSong; }

    public LocalDate getFavoriteDate() { return favoriteDate; }
    public void setFavoriteDate(LocalDate favoriteDate) { this.favoriteDate = favoriteDate; }

    public Integer getFavoriteNumber() { return favoriteNumber; }
    public void setFavoriteNumber(Integer favoriteNumber) { this.favoriteNumber = favoriteNumber; }
}