package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.alexandr.BookingCinemaTickets.domain.enums.Rating;
import ru.alexandr.BookingCinemaTickets.domain.model.Movie;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.PostgreSQLTestContainer;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@PostgreSQLTestContainer
class MovieRepositoryTest {
    @Autowired
    MovieRepository movieRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = movieRepository.save(new Movie("Test Movie", "desc", 120, LocalDate.now(), Rating.G));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findById_ShouldReturnMovie_WhenExists() {
        Optional<Movie> found = movieRepository.findById(movie.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Test Movie");
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<Movie> found = movieRepository.findById(-1L);
        assertThat(found).isEmpty();
    }
} 