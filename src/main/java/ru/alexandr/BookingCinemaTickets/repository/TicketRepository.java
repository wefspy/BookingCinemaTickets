package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.alexandr.BookingCinemaTickets.domain.Ticket;

@RepositoryRestResource
public interface TicketRepository extends CrudRepository<Ticket, Long> {
}
