package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.model.SessionSeat;

import java.util.Optional;

public interface SessionSeatRepository extends JpaRepository<SessionSeat, Long> {
    Optional<SessionSeat> findByIdAndSessionId(Long id, Long sessionId);

    void deleteByIdAndSessionId(Long id, Long sessionId);
}
