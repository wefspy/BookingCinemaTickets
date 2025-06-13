package ru.alexandr.BookingCinemaTickets.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;

public record UserRegisterDto(
        @NotBlank(message = "Username обязательное поле")
        String username,

        @NotBlank(message = "Пароль обязательное поле")
        @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
        String password,

        @NotEmpty(message = "Должен быть указан хотя бы один идентификатор роли")
        Set<Long> roleIds,

        @Email(message = "Email не валидный")
        String email,

        @Size(min = 10, max = 15, message = "Номер телефона должен содержать от 10 до 15 символов")
        String phoneNumber,

        LocalDateTime createdAt
) {
}
