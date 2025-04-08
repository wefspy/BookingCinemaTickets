package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.Ticket;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
}
