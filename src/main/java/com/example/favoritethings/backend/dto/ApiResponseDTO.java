package com.example.favoritethings.backend.dto;

/**
 * Универсальный формат ответа API.
 */
public class ApiResponseDTO {

    private boolean success;
    private Object data; // Заменяем String message на Object data для универсальности

    public ApiResponseDTO() {}

    public ApiResponseDTO(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    // Геттеры и сеттеры
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}