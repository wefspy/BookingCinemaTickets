package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.Ticket;

import java.util.UUID;

public interface TicketRepository extends CrudRepository<Ticket, UUID> {
}
