package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.model.SessionSeat;

public interface SessionSeatRepository extends CrudRepository<SessionSeat, Long> {
}
