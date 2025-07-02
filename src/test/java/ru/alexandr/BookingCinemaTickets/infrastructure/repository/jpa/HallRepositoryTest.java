package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.alexandr.BookingCinemaTickets.domain.enums.SoundSystem;
import ru.alexandr.BookingCinemaTickets.domain.model.Hall;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.PostgreSQLTestContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@PostgreSQLTestContainer
class HallRepositoryTest {
    @Autowired
    HallRepository hallRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Hall hall;

    @BeforeEach
    void setUp() {
        hall = hallRepository.save(new Hall("Test Hall", SoundSystem.DOLBY_ATMOS));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findById_ShouldReturnHall_WhenExists() {
        Optional<Hall> found = hallRepository.findById(hall.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Hall");
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<Hall> found = hallRepository.findById(-1L);
        assertThat(found).isEmpty();
    }
} 