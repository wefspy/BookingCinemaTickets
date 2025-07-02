package ru.alexandr.BookingCinemaTickets.application.dto.movie;

import ru.alexandr.BookingCinemaTickets.domain.enums.Rating;

import java.time.LocalDate;

public sealed interface MovieData permits MovieDto, MovieCreateDto {
    String title();
    String description();
    Integer durationInMinutes();
    LocalDate releaseDate();
    Rating rating();
}
