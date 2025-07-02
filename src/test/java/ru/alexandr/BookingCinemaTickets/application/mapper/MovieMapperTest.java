package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreDto;
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieData;
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieDto;
import ru.alexandr.BookingCinemaTickets.domain.enums.Rating;
import ru.alexandr.BookingCinemaTickets.domain.model.Genre;
import ru.alexandr.BookingCinemaTickets.domain.model.GenreMovie;
import ru.alexandr.BookingCinemaTickets.domain.model.Movie;
import ru.alexandr.BookingCinemaTickets.testUtils.factory.TestEntityFactory;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MovieMapperTest {
    @Mock
    private GenreMapper genreMapper;

    private MovieMapper movieMapper;
    private Movie movie;
    private MovieData movieData;
    private Genre genre;
    private GenreDto genreDto;

    @BeforeEach
    void setUp() {
        movieMapper = new MovieMapper(genreMapper);

        movieData = new MovieCreateDto(
                "Test Movie",
                "Test Description",
                120,
                LocalDate.of(2024, 1, 1),
                Rating.PG,
                List.of(1L)
        );

        movie = TestEntityFactory.movie(
                1L,
                movieData.title(),
                movieData.description(),
                movieData.durationInMinutes(),
                movieData.releaseDate(),
                movieData.rating()
        );

        genre = TestEntityFactory.genre(1L, "Action", "Action movies");
        genreDto = new GenreDto(1L, "Action", "Action movies");

        GenreMovie genreMovie = TestEntityFactory.genreMovie(1L, movie, genre);
        movie.getGenreMovie().add(genreMovie);
    }

    @Test
    void toEntity_ShouldMapCorrectly() {
        Movie result = movieMapper.toEntity(movieData);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getTitle()).isEqualTo(movieData.title());
        Assertions.assertThat(result.getDescription()).isEqualTo(movieData.description());
        Assertions.assertThat(result.getDurationInMinutes()).isEqualTo(movieData.durationInMinutes());
        Assertions.assertThat(result.getReleaseDate()).isEqualTo(movieData.releaseDate());
        Assertions.assertThat(result.getRating()).isEqualTo(movieData.rating());
    }

    @Test
    void toDto_WhenGivenMovie_ShouldMapCorrectly() {
        Mockito.when(genreMapper.toDto(genre)).thenReturn(genreDto);

        MovieDto result = movieMapper.toDto(movie);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.id()).isEqualTo(movie.getId());
        Assertions.assertThat(result.title()).isEqualTo(movie.getTitle());
        Assertions.assertThat(result.description()).isEqualTo(movie.getDescription());
        Assertions.assertThat(result.durationInMinutes()).isEqualTo(movie.getDurationInMinutes());
        Assertions.assertThat(result.releaseDate()).isEqualTo(movie.getReleaseDate());
        Assertions.assertThat(result.rating()).isEqualTo(movie.getRating());
        Assertions.assertThat(result.genres()).hasSize(1);
        Assertions.assertThat(result.genres().get(0)).isEqualTo(genreDto);
    }

    @Test
    void toDto_WhenGivenPageOfMovies_ShouldMapCorrectly() {
        Mockito.when(genreMapper.toDto(genre)).thenReturn(genreDto);
        Page<Movie> moviePage = new PageImpl<>(List.of(movie));

        Page<MovieDto> result = movieMapper.toDto(moviePage);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent()).hasSize(1);
        MovieDto movieDto = result.getContent().get(0);
        Assertions.assertThat(movieDto.id()).isEqualTo(movie.getId());
        Assertions.assertThat(movieDto.title()).isEqualTo(movie.getTitle());
        Assertions.assertThat(movieDto.genres()).hasSize(1);
        Assertions.assertThat(movieDto.genres().get(0)).isEqualTo(genreDto);
    }
} 