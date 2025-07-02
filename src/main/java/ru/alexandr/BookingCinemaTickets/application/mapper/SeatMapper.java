package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.springframework.stereotype.Component;
import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatData;
import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatDto;
import ru.alexandr.BookingCinemaTickets.domain.model.Hall;
import ru.alexandr.BookingCinemaTickets.domain.model.Seat;

import java.util.Collection;
import java.util.List;

@Component
public class SeatMapper {

    public Seat toEntity(Hall hall, SeatData seatData) {
        return new Seat(hall, seatData.rowNumber(), seatData.seatNumber(), seatData.type());
    }

    public List<Seat> toEntity(Hall hall, List<? extends SeatData> seatsData) {
        return seatsData.stream()
                .map(sd -> toEntity(hall, sd))
                .toList();
    }

    public SeatDto toDto(Seat seat) {
        return new SeatDto(seat.getId(), seat.getRowNumber(), seat.getSeatNumber(), seat.getType());
    }

    public List<SeatDto> toDto(Collection<Seat> seats) {
        return seats.stream()
                .map(this::toDto)
                .toList();
    }
}
