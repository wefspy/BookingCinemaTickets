package ru.alexandr.BookingCinemaTickets.application.dto.seat;

import ru.alexandr.BookingCinemaTickets.domain.enums.SeatType;

public sealed interface SeatData permits SeatDto, SeatCreateDto {
    Integer rowNumber();
    Integer seatNumber();
    SeatType type();
}
