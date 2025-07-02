package ru.alexandr.BookingCinemaTickets.application.dto.hall;

import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatCreateDto;
import ru.alexandr.BookingCinemaTickets.domain.enums.SoundSystem;

import java.util.List;

public record HallCreateDto(
        String name,
        SoundSystem soundSystem,
        List<SeatCreateDto> seats
) implements HallData {
}
