package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreDto;
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieData;
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieDto;
import ru.alexandr.BookingCinemaTickets.domain.model.GenreMovie;
import ru.alexandr.BookingCinemaTickets.domain.model.Movie;

import java.util.List;

@Component
public class MovieMapper {
    private final GenreMapper genreMapper;

    public MovieMapper(GenreMapper genreMapper) {
        this.genreMapper = genreMapper;
    }

    public Movie toEntity(MovieData data) {
        return new Movie(
                data.title(),
                data.description(),
                data.durationInMinutes(),
                data.releaseDate(),
                data.rating()
        );
    }

    public MovieDto toDto(Movie movieWithGenres) {
        List<GenreDto> genreDtos = movieWithGenres.getGenreMovie().stream()
                .map(GenreMovie::getGenre)
                .map(genreMapper::toDto)
                .toList();
        return new MovieDto(
                movieWithGenres.getId(),
                movieWithGenres.getTitle(),
                movieWithGenres.getDescription(),
                movieWithGenres.getDurationInMinutes(),
                movieWithGenres.getReleaseDate(),
                movieWithGenres.getRating(),
                genreDtos
        );
    }

    public Page<MovieDto> toDto(Page<Movie> moviesWithGenres) {
        return moviesWithGenres.map(this::toDto);
    }
}
