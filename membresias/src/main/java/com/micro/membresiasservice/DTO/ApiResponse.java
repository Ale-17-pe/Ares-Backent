package com.micro.membresiasservice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Clase genérica para estandarizar las respuestas de todos los endpoints.
 * Incluye timestamp, mensaje, estado HTTP y datos genéricos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true) // ✅ habilita el método .toBuilder()
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private LocalDateTime timestamp;
    private int statusCode;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(200)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(200)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(400)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(int statusCode, String message) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(statusCode)
                .message(message)
                .build();
    }
}
