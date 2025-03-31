package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.Movie;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MovieRepository extends CrudRepository<Movie, UUID> {
    List<Movie> findByReleaseDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
