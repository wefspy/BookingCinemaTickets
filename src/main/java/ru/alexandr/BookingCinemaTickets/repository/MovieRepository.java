package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.Movie;

import java.time.LocalDateTime;
import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, Long> {
    List<Movie> findByReleaseDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
