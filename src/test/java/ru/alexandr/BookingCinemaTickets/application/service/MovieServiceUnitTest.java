package ru.alexandr.BookingCinemaTickets.application.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieDto;
import ru.alexandr.BookingCinemaTickets.application.mapper.MovieMapper;
import ru.alexandr.BookingCinemaTickets.domain.enums.Rating;
import ru.alexandr.BookingCinemaTickets.domain.model.Movie;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.MovieRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceUnitTest {
    @InjectMocks
    private MovieService movieService;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private MovieMapper movieMapper;

    @Test
    void get_ShouldReturnMovieDto_WhenExists() {
        Movie movie = new Movie("Test Movie", "desc", 120, LocalDate.now(), Rating.G);
        MovieDto movieDto = new MovieDto(1L, "Test Movie", "desc", 120, LocalDate.now(), Rating.G, null);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieMapper.toDto(movie)).thenReturn(movieDto);

        MovieDto result = movieService.getById(1L);

        assertThat(result).isEqualTo(movieDto);
        verify(movieRepository).findById(1L);
    }

    @Test
    void get_ShouldThrowException_WhenNotExists() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> movieService.getById(1L))
                .isInstanceOf(RuntimeException.class);

        verify(movieRepository).findById(1L);
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        movieService.delete(1L);
        verify(movieRepository).deleteById(1L);
    }
} 