package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.Hall;

import java.util.UUID;

public interface HallRepository extends CrudRepository<Hall, UUID> {
}
