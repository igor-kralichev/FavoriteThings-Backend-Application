package com.example.favoritethings.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO для регистрации пользователя.
 */
public class RegisterRequest {

    @NotBlank(message = "ФИО не должно быть пустым")
    @Pattern(regexp = "^[А-ЯЁа-яё\\s]+$", message = "ФИО должно содержать только русские буквы и пробелы")
    private String fullName;     // ФИО (проверка на кириллицу выполняется на уровне сервиса или через аннотации)
    
    @NotNull(message = "Дата рождения обязательна")
    private LocalDate birthDate; // Дата рождения

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;        // Email (уникальный, проверка формата)
    
    private String password;     // Пароль

    // Геттеры и сеттеры
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
