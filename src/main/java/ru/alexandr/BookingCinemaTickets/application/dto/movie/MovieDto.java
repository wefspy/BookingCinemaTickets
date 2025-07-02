package ru.alexandr.BookingCinemaTickets.application.dto.movie;

import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreDto;
import ru.alexandr.BookingCinemaTickets.domain.enums.Rating;

import java.time.LocalDate;
import java.util.List;

public record MovieDto(
        Long id,
        String title,
        String description,
        Integer durationInMinutes,
        LocalDate releaseDate,
        Rating rating,
        List<GenreDto> genres
) implements MovieData {
}
