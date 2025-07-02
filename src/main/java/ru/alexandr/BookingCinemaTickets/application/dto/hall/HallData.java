package ru.alexandr.BookingCinemaTickets.application.dto.hall;

import ru.alexandr.BookingCinemaTickets.domain.enums.SoundSystem;

public sealed interface HallData permits HallDto, HallCreateDto, HallUpdateDto, HallPreviewDto {
    String name();
    SoundSystem soundSystem();
}
