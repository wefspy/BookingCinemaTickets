package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.alexandr.BookingCinemaTickets.domain.enums.Rating;
import ru.alexandr.BookingCinemaTickets.domain.model.Movie;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Movie movie1;
    private Movie movie2;
    private Movie movie3;

    @BeforeEach
    void setUp() {
        assertThat(movieRepository.count()).isZero();

        movie1 = movieRepository.save(new Movie(
                "title1",
                1,
                LocalDateTime.of(2024, 3, 10, 12, 0),
                Rating.R
        ));
        movie2 = movieRepository.save(new Movie(
                "title2",
                2,
                LocalDateTime.of(2024, 7, 15, 18, 30),
                Rating.R
        ));

        movie3 = movieRepository.save(new Movie(
                "title3",
                3,
                LocalDateTime.of(2023, 11, 20, 15, 0),
                Rating.R
        ));

        entityManager.flush();
    }

    @Test
    void findByReleaseDateBetween_ShouldReturnMoviesWithinDateRange() {
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);

        List<Movie> movies = movieRepository.findByReleaseDateBetween(startDate, endDate);

        assertThat(movies).hasSize(2)
                .extracting(Movie::getTitle)
                .containsExactlyInAnyOrder(movie1.getTitle(), movie2.getTitle());
    }

    @Test
    void findByReleaseDateBetween_ShouldReturnEmptyList_WhenNoMoviesMatch() {
        LocalDateTime futureStart = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime futureEnd = LocalDateTime.of(2025, 12, 31, 23, 59);

        List<Movie> movies = movieRepository.findByReleaseDateBetween(futureStart, futureEnd);

        assertThat(movies).isEmpty();
    }
}