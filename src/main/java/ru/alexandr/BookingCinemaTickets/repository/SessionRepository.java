package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.Session;

import java.util.UUID;

public interface SessionRepository extends CrudRepository<Session, UUID> {
}
