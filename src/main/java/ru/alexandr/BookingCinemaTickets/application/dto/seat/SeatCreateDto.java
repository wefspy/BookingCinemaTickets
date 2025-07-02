package ru.alexandr.BookingCinemaTickets.application.dto.seat;

import ru.alexandr.BookingCinemaTickets.domain.enums.SeatType;

public record SeatCreateDto(
        Integer rowNumber,
        Integer seatNumber,
        SeatType type
) implements SeatData {
}
