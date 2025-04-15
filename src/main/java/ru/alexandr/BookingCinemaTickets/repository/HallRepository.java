package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.alexandr.BookingCinemaTickets.domain.Hall;

@RepositoryRestResource
public interface HallRepository extends CrudRepository<Hall, Long> {
}
