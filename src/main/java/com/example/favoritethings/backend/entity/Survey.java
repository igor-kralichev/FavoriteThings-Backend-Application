package com.example.favoritethings.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Сущность для хранения ответов опроса.
 */
@Entity
@Table(name = "surveys")
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String favoriteFood;
    private String favoriteColor;
    private String favoriteSong;
    private LocalDate favoriteDate;
    private Integer favoriteNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
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
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
