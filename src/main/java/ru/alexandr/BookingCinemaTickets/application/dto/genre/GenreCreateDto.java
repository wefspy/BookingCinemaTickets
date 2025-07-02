package ru.alexandr.BookingCinemaTickets.application.dto.genre;

public record GenreCreateDto(
        String name,
        String description
) implements GenreData {
}
