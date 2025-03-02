package com.example.favoritethings.backend.dto;

import java.time.LocalDate;

/**
 * DTO для заполнения формы опроса.
 */
public class SurveyRequest {

    private String favoriteFood;
    private String favoriteColor; // HEX значение (например, "#FFFFFF")
    private String favoriteSong;
    private LocalDate favoriteDate;
    private Integer favoriteNumber;

    // Геттеры и сеттеры
    public String getFavoriteFood() {
        return favoriteFood;
    }
    public void setFavoriteFood(String favoriteFood) {
        this.favoriteFood = favoriteFood;
    }
    public String getFavoriteColor() {
        return favoriteColor;
    }
    public void setFavoriteColor(String favoriteColor) {
        this.favoriteColor = favoriteColor;
    }
    public String getFavoriteSong() {
        return favoriteSong;
    }
    public void setFavoriteSong(String favoriteSong) {
        this.favoriteSong = favoriteSong;
    }
    public LocalDate getFavoriteDate() {
        return favoriteDate;
    }
    public void setFavoriteDate(LocalDate favoriteDate) {
        this.favoriteDate = favoriteDate;
    }
    public Integer getFavoriteNumber() {
        return favoriteNumber;
    }
    public void setFavoriteNumber(Integer favoriteNumber) {
        this.favoriteNumber = favoriteNumber;
    }
}
