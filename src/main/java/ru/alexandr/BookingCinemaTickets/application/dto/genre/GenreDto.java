package ru.alexandr.BookingCinemaTickets.application.dto.genre;

public record GenreDto(
        Long id,
        String name,
        String description
) implements GenreData {
}
