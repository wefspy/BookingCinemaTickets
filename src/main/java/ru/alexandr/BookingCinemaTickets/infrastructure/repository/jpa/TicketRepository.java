package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.model.Ticket;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
}
