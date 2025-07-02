package ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat;

import ru.alexandr.BookingCinemaTickets.domain.enums.SessionSeatStatus;

import java.math.BigDecimal;

public record SessionSeatUpdateDto(
        BigDecimal price,
        SessionSeatStatus status
) implements SessionSeatData {
}
