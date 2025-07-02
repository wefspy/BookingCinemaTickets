package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.alexandr.BookingCinemaTickets.domain.enums.SeatType;
import ru.alexandr.BookingCinemaTickets.domain.enums.SessionSeatStatus;
import ru.alexandr.BookingCinemaTickets.domain.enums.SoundSystem;
import ru.alexandr.BookingCinemaTickets.domain.model.Hall;
import ru.alexandr.BookingCinemaTickets.domain.model.Movie;
import ru.alexandr.BookingCinemaTickets.domain.model.Seat;
import ru.alexandr.BookingCinemaTickets.domain.model.Session;
import ru.alexandr.BookingCinemaTickets.domain.model.SessionSeat;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.PostgreSQLTestContainer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@PostgreSQLTestContainer
class SessionSeatRepositoryTest {
    @Autowired
    SessionSeatRepository sessionSeatRepository;
    @Autowired
    HallRepository hallRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    SessionRepository sessionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private SessionSeat sessionSeat;

    @BeforeEach
    void setUp() {
        Hall hall = hallRepository.save(new Hall("Test Hall", SoundSystem.DOLBY_ATMOS));
        Movie movie = movieRepository.save(new Movie("Test Movie", "desc", 120, LocalDate.now(), ru.alexandr.BookingCinemaTickets.domain.enums.Rating.G));
        Seat seat = seatRepository.save(new Seat(hall, 1, 1, SeatType.STANDARD));
        Session session = sessionRepository.save(new Session(movie, hall, LocalDateTime.now()));
        sessionSeat = sessionSeatRepository.save(new SessionSeat(session, seat, BigDecimal.valueOf(100), SessionSeatStatus.AVAILABLE));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findById_ShouldReturnSessionSeat_WhenExists() {
        Optional<SessionSeat> found = sessionSeatRepository.findById(sessionSeat.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getStatus()).isEqualTo(SessionSeatStatus.AVAILABLE);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<SessionSeat> found = sessionSeatRepository.findById(-1L);
        assertThat(found).isEmpty();
    }
} 