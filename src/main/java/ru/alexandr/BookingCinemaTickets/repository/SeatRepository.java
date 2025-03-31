package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.Seat;

import java.util.UUID;

public interface SeatRepository extends CrudRepository<Seat, UUID> {
}
