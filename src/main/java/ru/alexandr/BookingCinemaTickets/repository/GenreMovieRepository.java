package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.alexandr.BookingCinemaTickets.domain.GenreMovie;

@RepositoryRestResource
public interface GenreMovieRepository extends CrudRepository<GenreMovie, Long> {
}
