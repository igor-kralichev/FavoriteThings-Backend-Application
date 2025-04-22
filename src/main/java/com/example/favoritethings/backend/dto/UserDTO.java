package com.example.favoritethings.backend.dto;

import java.time.LocalDate;

public class UserDTO {

    private Long id;
    private String fullName;
    private LocalDate birthDate;
    private String email;
    private boolean emailConfirmed;
    private String role;
    private Object surveys; // Может быть List<SurveyDTO> или String

    // Конструкторы
    public UserDTO() {}

    public UserDTO(Long id, String fullName, LocalDate birthDate, String email, boolean emailConfirmed, String role, Object surveys) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.email = email;
        this.emailConfirmed = emailConfirmed;
        this.role = role;
        this.surveys = surveys;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isEmailConfirmed() { return emailConfirmed; }
    public void setEmailConfirmed(boolean emailConfirmed) { this.emailConfirmed = emailConfirmed; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Object getSurveys() { return surveys; }
    public void setSurveys(Object surveys) { this.surveys = surveys; }
}