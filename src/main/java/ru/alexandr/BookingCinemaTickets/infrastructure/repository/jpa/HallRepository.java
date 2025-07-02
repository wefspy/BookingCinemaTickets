package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.model.Hall;

import java.util.Optional;

public interface HallRepository extends JpaRepository<Hall, Long> {

    @Query("SELECT h FROM Hall h WHERE h.id = :id ")
    @EntityGraph(attributePaths = "seats")
    Optional<Hall> findByIdWithSeats(Long id);
}
