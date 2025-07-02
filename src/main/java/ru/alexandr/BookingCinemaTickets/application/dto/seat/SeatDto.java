package ru.alexandr.BookingCinemaTickets.application.dto.seat;

import ru.alexandr.BookingCinemaTickets.domain.enums.SeatType;

public record SeatDto(
        Long id,
        Integer rowNumber,
        Integer seatNumber,
        SeatType type
) implements SeatData{
}
