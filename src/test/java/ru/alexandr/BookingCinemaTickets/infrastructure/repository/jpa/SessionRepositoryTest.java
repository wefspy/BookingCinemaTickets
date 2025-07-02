package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.alexandr.BookingCinemaTickets.domain.enums.Rating;
import ru.alexandr.BookingCinemaTickets.domain.enums.SoundSystem;
import ru.alexandr.BookingCinemaTickets.domain.model.Hall;
import ru.alexandr.BookingCinemaTickets.domain.model.Movie;
import ru.alexandr.BookingCinemaTickets.domain.model.Session;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.PostgreSQLTestContainer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@PostgreSQLTestContainer
class SessionRepositoryTest {
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    HallRepository hallRepository;
    @Autowired
    MovieRepository movieRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Session session;

    @BeforeEach
    void setUp() {
        Hall hall = hallRepository.save(new Hall("Test Hall", SoundSystem.DOLBY_ATMOS));
        Movie movie = movieRepository.save(new Movie("Test Movie", "desc", 120, LocalDate.now(), Rating.G));
        session = sessionRepository.save(new Session(movie, hall, LocalDateTime.now()));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findById_ShouldReturnSession_WhenExists() {
        Optional<Session> found = sessionRepository.findById(session.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(session.getId());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<Session> found = sessionRepository.findById(-1L);
        assertThat(found).isEmpty();
    }
} 