package ru.alexandr.BookingCinemaTickets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.alexandr.BookingCinemaTickets.domain.Movie;
import ru.alexandr.BookingCinemaTickets.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.service.exception.EntityNotFoundException;
import ru.alexandr.BookingCinemaTickets.service.dto.MovieDto;
import ru.alexandr.BookingCinemaTickets.service.mapper.MovieMapper;

import java.util.List;

@Service
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public class MovieServiceImpl implements MovieService {
    private final CrudRepository<Movie, Long> movieRepository;

    public MovieServiceImpl(@Autowired CrudRepository<Movie, Long> movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void create(MovieDto movieDto) {
        Movie movie = MovieMapper.toMovie(movieDto);
        movieRepository.create(movie);
    }

    @Override
    public Movie findById(Long id) throws EntityNotFoundException {
        Movie movie = movieRepository.read(id);
        if (movie == null) {
            throw new EntityNotFoundException("Фильма с указанным Id не существует: " + id);
        }

        return movie;
    }

    @Override
    public List<Movie> findAll() {
        return movieRepository.readAll();
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {
        if (movieRepository.read(id) == null) {
            throw new EntityNotFoundException("Фильма с указанным Id не существует: " + id);
        }
        movieRepository.delete(id);
    }

    @Override
    public void update(Movie movie) throws EntityNotFoundException {
        Movie oldMovie = movieRepository.read(movie.getId());

        if (oldMovie == null) {
            throw new EntityNotFoundException("Указанного фильма не существует: " + movie);
        }

        oldMovie.setTitle(movie.getTitle());
        oldMovie.setDescription(movie.getDescription());
        oldMovie.setDurationInMinutes(movie.getDurationInMinutes());
        oldMovie.setReleaseDate(movie.getReleaseDate());
        oldMovie.setRating(movie.getRating());
        oldMovie.setPosterUrl(movie.getPosterUrl());

        movieRepository.update(movie);
    }
}
