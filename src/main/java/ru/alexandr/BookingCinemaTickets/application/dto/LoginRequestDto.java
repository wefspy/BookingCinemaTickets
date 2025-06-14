package ru.alexandr.BookingCinemaTickets.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на аутентификацию")
public record LoginRequestDto(
        @Schema(description = "Имя пользователя", example = "John")
        @NotBlank(message = "Username обязательное поле")
        String username,

        @Schema(description = "Пароль", example = "myStrongPassword")
        @NotBlank(message = "Пароль не может быть пустыми")
        String password
) {
}
