package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.alexandr.BookingCinemaTickets.domain.model.Genre;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.PostgreSQLTestContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@PostgreSQLTestContainer
class GenreRepositoryTest {
    @Autowired
    GenreRepository genreRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Genre genre;

    @BeforeEach
    void setUp() {
        genre = genreRepository.save(new Genre("Test Genre", "desc"));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findById_ShouldReturnGenre_WhenExists() {
        Optional<Genre> found = genreRepository.findById(genre.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Genre");
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<Genre> found = genreRepository.findById(-1L);
        assertThat(found).isEmpty();
    }
} 