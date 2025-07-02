package ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat;

import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatDto;
import ru.alexandr.BookingCinemaTickets.domain.enums.SessionSeatStatus;

import java.math.BigDecimal;

public record SessionSeatDto(
        Long id,
        BigDecimal price,
        SessionSeatStatus status,
        SeatDto seat
) implements SessionSeatData {
}
