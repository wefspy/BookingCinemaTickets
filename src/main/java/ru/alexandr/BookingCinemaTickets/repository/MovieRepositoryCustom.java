package ru.alexandr.BookingCinemaTickets.repository;

import ru.alexandr.BookingCinemaTickets.domain.Movie;

import java.time.LocalDateTime;
import java.util.List;

public interface MovieRepositoryCustom {
    List<Movie> findByReleaseDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
