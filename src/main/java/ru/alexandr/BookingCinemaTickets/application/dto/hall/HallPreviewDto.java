package ru.alexandr.BookingCinemaTickets.application.dto.hall;

import ru.alexandr.BookingCinemaTickets.domain.enums.SoundSystem;

public record HallPreviewDto(
        Long id,
        String name,
        SoundSystem soundSystem
) implements HallData {
}
