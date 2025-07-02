package ru.alexandr.BookingCinemaTickets.application.dto.hall;

import ru.alexandr.BookingCinemaTickets.domain.enums.SoundSystem;

public record HallUpdateDto(
        String name,
        SoundSystem soundSystem
) implements HallData {
}
