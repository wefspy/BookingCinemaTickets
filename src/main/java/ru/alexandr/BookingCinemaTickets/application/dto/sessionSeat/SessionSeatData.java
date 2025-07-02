package ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat;

import ru.alexandr.BookingCinemaTickets.domain.enums.SessionSeatStatus;

import java.math.BigDecimal;

public sealed interface SessionSeatData permits SessionSeatDto, SessionSeatUpdateDto, SessionSeatCreateDto {
    BigDecimal price();
    SessionSeatStatus status();
}
