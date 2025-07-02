package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.alexandr.BookingCinemaTickets.domain.enums.SeatType;
import ru.alexandr.BookingCinemaTickets.domain.enums.SoundSystem;
import ru.alexandr.BookingCinemaTickets.domain.model.Hall;
import ru.alexandr.BookingCinemaTickets.domain.model.Seat;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.PostgreSQLTestContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@PostgreSQLTestContainer
class SeatRepositoryTest {
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    HallRepository hallRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Seat seat;

    @BeforeEach
    void setUp() {
        Hall hall = hallRepository.save(new Hall("Test Hall", SoundSystem.DOLBY_ATMOS));
        seat = seatRepository.save(new Seat(hall, 1, 1, SeatType.STANDARD));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findById_ShouldReturnSeat_WhenExists() {
        Optional<Seat> found = seatRepository.findById(seat.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getRowNumber()).isEqualTo(1);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<Seat> found = seatRepository.findById(-1L);
        assertThat(found).isEmpty();
    }
} 