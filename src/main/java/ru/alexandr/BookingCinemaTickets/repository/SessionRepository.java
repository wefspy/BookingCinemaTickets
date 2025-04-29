package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.Session;

public interface SessionRepository extends CrudRepository<Session, Long> {
}
