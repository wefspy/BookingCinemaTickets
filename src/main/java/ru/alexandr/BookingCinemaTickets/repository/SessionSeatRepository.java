package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.SessionSeat;

import java.util.UUID;

public interface SessionSeatRepository extends CrudRepository<SessionSeat, UUID> {
}
