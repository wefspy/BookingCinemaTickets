package ru.alexandr.BookingCinemaTickets.application.dto.session;

import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatCreateDto;

import java.time.LocalDateTime;
import java.util.List;

public record SessionCreateDto(
        Long movieId,
        Long hallId,
        LocalDateTime startTime,
        List<SessionSeatCreateDto> seats
) implements SessionData {
}
