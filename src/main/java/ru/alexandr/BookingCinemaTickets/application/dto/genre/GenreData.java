package ru.alexandr.BookingCinemaTickets.application.dto.genre;

public sealed interface GenreData permits GenreDto, GenreCreateDto {
    String name();
    String description();
}
