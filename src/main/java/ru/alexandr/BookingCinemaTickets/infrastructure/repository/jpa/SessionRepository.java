package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.model.Session;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    @EntityGraph(attributePaths = {"hall", "movie"})
    Page<Session> findAllByHallId(Long hallId, Pageable pageable);

    @EntityGraph(attributePaths = {"hall", "movie"})
    Page<Session> findAllByMovieId(Long hallId, Pageable pageable);

    @Query("SELECT s FROM Session s WHERE s.id = :id ")
    @EntityGraph(attributePaths = {"hall", "movie", "sessionSeat"})
    Optional<Session> findByIdWithHallAndMovieAndSessionSeat(Long id);

    @Query("SELECT s FROM Session s WHERE s.id = :id ")
    @EntityGraph(attributePaths = {"hall", "movie"})
    Optional<Session> findByIdWithHallAndMovie(Long id);
}
