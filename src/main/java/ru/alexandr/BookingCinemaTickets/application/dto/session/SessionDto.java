package ru.alexandr.BookingCinemaTickets.application.dto.session;

import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallDto;
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieDto;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatDto;

import java.time.LocalDateTime;
import java.util.List;

public record SessionDto(
        Long id,
        MovieDto movie,
        HallDto hall,
        LocalDateTime startTime,
        List<SessionSeatDto> seats
) implements SessionData {

}
