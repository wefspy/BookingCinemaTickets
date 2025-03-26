package ru.alexandr.BookingCinemaTickets.service.mapper;


import ru.alexandr.BookingCinemaTickets.domain.Movie;
import ru.alexandr.BookingCinemaTickets.service.dto.MovieDto;

public final class MovieMapper {
    private MovieMapper() {}

    public static Movie toMovie(MovieDto movieDto) {
        return new Movie(
                null,
                movieDto.title(),
                movieDto.description(),
                movieDto.durationInMinutes(),
                movieDto.releaseDate(),
                movieDto.rating(),
                movieDto.posterUrl()
        );
    }

    public static MovieDto toDto(Movie movie) {
        return new MovieDto(
                movie.getTitle(),
                movie.getDescription(),
                movie.getDurationInMinutes(),
                movie.getReleaseDate(),
                movie.getRating(),
                movie.getPosterUrl()
        );
    }
}
