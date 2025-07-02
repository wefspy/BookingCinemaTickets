package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionData;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionDto;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionPreviewDto;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatDto;
import ru.alexandr.BookingCinemaTickets.domain.model.Hall;
import ru.alexandr.BookingCinemaTickets.domain.model.Movie;
import ru.alexandr.BookingCinemaTickets.domain.model.Session;

import java.util.List;

@Component
public class SessionMapper {
    private final MovieMapper movieMapper;
    private final HallMapper hallMapper;
    private final SessionSeatMapper sessionSeatMapper;

    public SessionMapper(MovieMapper movieMapper, HallMapper hallMapper, SessionSeatMapper sessionSeatMapper) {
        this.movieMapper = movieMapper;
        this.hallMapper = hallMapper;
        this.sessionSeatMapper = sessionSeatMapper;
    }

    public Session toEntity(Hall hall, Movie movie, SessionData data) {
        return new Session(movie, hall, data.startTime());
    }

    public SessionDto toDto(Session sessionWithMovieAndHallAndSessionSeat) {
        List<SessionSeatDto> sessionSeats = sessionWithMovieAndHallAndSessionSeat.getSessionSeat().stream()
                .map(sessionSeatMapper::toDto)
                .toList();
        return toDto(sessionWithMovieAndHallAndSessionSeat, sessionSeats);
    }

    public SessionDto toDto(Session sessionWithMovieAndHall, List<SessionSeatDto> sessionSeats) {
        return new SessionDto(
                sessionWithMovieAndHall.getId(),
                movieMapper.toDto(sessionWithMovieAndHall.getMovie()),
                hallMapper.toDto(sessionWithMovieAndHall.getHall()),
                sessionWithMovieAndHall.getStartTime(),
                sessionSeats
        );
    }

    public SessionPreviewDto toPreviewDto(Session sessionWithMovieAndHall) {
        return new SessionPreviewDto(
                sessionWithMovieAndHall.getId(),
                movieMapper.toDto(sessionWithMovieAndHall.getMovie()),
                hallMapper.toDto(sessionWithMovieAndHall.getHall()),
                sessionWithMovieAndHall.getStartTime()
        );
    }

    public Page<SessionPreviewDto> toPreviewDto(Page<Session> sessions) {
        return sessions.map(this::toPreviewDto);
    }
}
