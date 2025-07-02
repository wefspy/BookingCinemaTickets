package ru.alexandr.BookingCinemaTickets.application.dto.session;

import java.time.LocalDateTime;

public record SessionUpdateDto(
        Long movieId,
        Long hallId,
        LocalDateTime startTime
) implements SessionData {
}
