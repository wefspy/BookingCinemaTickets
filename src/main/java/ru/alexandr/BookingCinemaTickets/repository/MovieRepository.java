package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.alexandr.BookingCinemaTickets.domain.Movie;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface MovieRepository extends CrudRepository<Movie, Long> {
    List<Movie> findByReleaseDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
