package ru.alexandr.BookingCinemaTickets.application.dto.session;

import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallDto;
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieDto;

import java.time.LocalDateTime;

public record SessionPreviewDto(
        Long id,
        MovieDto movie,
        HallDto hall,
        LocalDateTime startTime
) implements SessionData {
}
