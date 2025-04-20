package ru.alexandr.BookingCinemaTickets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.alexandr.BookingCinemaTickets.dto.ApiErrorDto;
import ru.alexandr.BookingCinemaTickets.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.dto.UserRegisterDto;
import ru.alexandr.BookingCinemaTickets.service.AuthService;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Регистрация пользователя")
    @ApiResponse(responseCode = "200", description = "Пользователь зарегистрирован", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileInfoDto.class))
    })
    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Роли с указанными id не найдены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "409", description = "Указанный username уже занят", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @PostMapping("/api/register")
    public ResponseEntity<UserProfileInfoDto> createUserWithInfo(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        UserProfileInfoDto userProfileInfoDto = authService.createUserWithInfo(userRegisterDto);
        return ResponseEntity.status(200)
                .body(userProfileInfoDto);
    }
}
