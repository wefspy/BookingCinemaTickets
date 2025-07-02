package ru.alexandr.BookingCinemaTickets.application.dto.session;

import java.time.LocalDateTime;

public sealed interface SessionData permits SessionDto, SessionUpdateDto, SessionPreviewDto, SessionCreateDto {
    LocalDateTime startTime();
}
