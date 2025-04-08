package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.GenreMovie;

public interface GenreMovieRepository extends CrudRepository<GenreMovie, Long> {
}
