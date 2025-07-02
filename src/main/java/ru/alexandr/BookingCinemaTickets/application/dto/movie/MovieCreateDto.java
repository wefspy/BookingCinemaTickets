package ru.alexandr.BookingCinemaTickets.application.dto.movie;

import ru.alexandr.BookingCinemaTickets.domain.enums.Rating;

import java.time.LocalDate;
import java.util.List;

public record MovieCreateDto(
        String title,
        String description,
        Integer durationInMinutes,
        LocalDate releaseDate,
        Rating rating,
        List<Long> genreIds
) implements MovieData {
}
