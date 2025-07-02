package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.springframework.stereotype.Component;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatDto;
import ru.alexandr.BookingCinemaTickets.domain.model.Seat;
import ru.alexandr.BookingCinemaTickets.domain.model.Session;
import ru.alexandr.BookingCinemaTickets.domain.model.SessionSeat;

import java.util.List;

@Component
public class SessionSeatMapper {
    private final SeatMapper seatMapper;

    public SessionSeatMapper(SeatMapper seatMapper) {
        this.seatMapper = seatMapper;
    }

    public SessionSeat toEntity(Session session, Seat seat, SessionSeatCreateDto sessionSeat) {
        return new SessionSeat(
                session,
                seat,
                sessionSeat.price(),
                sessionSeat.status()
        );
    }

    public SessionSeatDto toDto(SessionSeat sessionSeatWithSeat) {
        return new SessionSeatDto(
                sessionSeatWithSeat.getId(),
                sessionSeatWithSeat.getPrice(),
                sessionSeatWithSeat.getStatus(),
                seatMapper.toDto(sessionSeatWithSeat.getSeat())
        );
    }

    public List<SessionSeatDto> toDto(List<SessionSeat> sessionSeats) {
        return sessionSeats.stream().map(this::toDto).toList();
    }
}
