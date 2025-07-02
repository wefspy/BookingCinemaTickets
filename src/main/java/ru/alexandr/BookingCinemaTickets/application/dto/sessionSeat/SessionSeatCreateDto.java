package ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat;

import ru.alexandr.BookingCinemaTickets.domain.enums.SessionSeatStatus;

import java.math.BigDecimal;

public record SessionSeatCreateDto(
        Long seatId,
        BigDecimal price,
        SessionSeatStatus status
) implements SessionSeatData {
}
