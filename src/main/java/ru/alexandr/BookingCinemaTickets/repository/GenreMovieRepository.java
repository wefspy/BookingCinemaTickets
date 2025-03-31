package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.GenreMovie;

import java.util.UUID;

public interface GenreMovieRepository extends CrudRepository<GenreMovie, UUID> {
}
