package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexandr.BookingCinemaTickets.domain.model.Seat;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findByIdAndHallId(Long seatId, Long hallId);
}
