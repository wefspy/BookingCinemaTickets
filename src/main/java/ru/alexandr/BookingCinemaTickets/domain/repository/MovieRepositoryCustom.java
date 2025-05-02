package ru.alexandr.BookingCinemaTickets.domain.repository;

import ru.alexandr.BookingCinemaTickets.domain.model.Movie;

import java.time.LocalDateTime;
import java.util.List;

public interface MovieRepositoryCustom {
    List<Movie> findByReleaseDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
