package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.Genre;

public interface GenreRepository extends CrudRepository<Genre, Long> {
}
