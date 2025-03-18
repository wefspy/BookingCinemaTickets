package ru.alexandr.BookingCinemaTickets.service;

import ru.alexandr.BookingCinemaTickets.domain.Movie;
import ru.alexandr.BookingCinemaTickets.service.dto.MovieDto;
import ru.alexandr.BookingCinemaTickets.service.exception.EntityNotFoundException;

import java.util.List;

public interface MovieService {
    void create(MovieDto movieDto);

    Movie findById(Long id) throws EntityNotFoundException;

    List<Movie> findAll();

    void delete(Long id) throws EntityNotFoundException;

    void update(Movie movie) throws EntityNotFoundException;
}
