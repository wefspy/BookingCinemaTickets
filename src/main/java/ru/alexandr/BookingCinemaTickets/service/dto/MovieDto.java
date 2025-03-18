package ru.alexandr.BookingCinemaTickets.service.dto;

import ru.alexandr.BookingCinemaTickets.domain.enums.Rating;

import java.net.URL;
import java.time.LocalDateTime;

public record MovieDto(
        String title,
        String description,
        Integer durationInMinutes,
        LocalDateTime releaseDate,
        Rating rating,
        URL posterUrl
) {
}
