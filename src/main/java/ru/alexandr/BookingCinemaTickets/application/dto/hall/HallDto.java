package ru.alexandr.BookingCinemaTickets.application.dto.hall;

import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatDto;
import ru.alexandr.BookingCinemaTickets.domain.enums.SoundSystem;

import java.util.List;

public record HallDto(
        Long id,
        String name,
        SoundSystem soundSystem,
        List<SeatDto> seats
) implements HallData {
}
