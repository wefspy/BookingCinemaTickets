package ru.alexandr.BookingCinemaTickets.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieDto;
import ru.alexandr.BookingCinemaTickets.application.exception.MovieNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.mapper.MovieMapper;
import ru.alexandr.BookingCinemaTickets.domain.model.Genre;
import ru.alexandr.BookingCinemaTickets.domain.model.Movie;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.GenreRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.MovieRepository;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieMapper movieMapper;

    public MovieService(MovieRepository movieRepository,
                        GenreRepository genreRepository,
                        MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.movieMapper = movieMapper;
    }

    @Transactional
    public MovieDto create(MovieCreateDto movieCreateDto) {
        Movie movie = movieMapper.toEntity(movieCreateDto);
        List<Genre> genres = genreRepository.findAllById(movieCreateDto.genreIds());
        movie.addGenres(genres);
        movie = movieRepository.save(movie);
        return movieMapper.toDto(movie);
    }

    @Transactional(readOnly = true)
    public Page<MovieDto> getAll(Pageable pageable) {
        Page<Movie> movies = movieRepository.findAllWithGenres(pageable);
        return movieMapper.toDto(movies);
    }

    @Transactional(readOnly = true)
    public MovieDto getById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(String.format("Фильм с id: %s не найден", id)));
        return movieMapper.toDto(movie);
    }

    @Transactional
    public MovieDto update(Long id, MovieCreateDto movieCreateDto) {
        Movie movie = movieRepository.findByIdWithGenres(id)
                .orElseThrow(() -> new MovieNotFoundException(String.format("Фильм с id: %s не найден", id)));
        List<Genre> genres = genreRepository.findAllById(movieCreateDto.genreIds());
        movie.update(movieCreateDto);
        movie.addGenres(genres);
        return movieMapper.toDto(movie);
    }

    @Transactional
    public void delete(Long movieId) {
        movieRepository.deleteById(movieId);
    }
}
