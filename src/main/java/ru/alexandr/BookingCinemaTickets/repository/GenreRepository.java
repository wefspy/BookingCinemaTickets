package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.Genre;

import java.util.UUID;

public interface GenreRepository extends CrudRepository<Genre, UUID> {
}
