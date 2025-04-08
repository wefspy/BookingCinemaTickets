package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.Hall;

public interface HallRepository extends CrudRepository<Hall, Long> {
}
