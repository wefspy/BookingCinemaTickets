package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.alexandr.BookingCinemaTickets.domain.model.Movie;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m FROM Movie m")
    @EntityGraph(attributePaths = "genreMovie.genre")
    Page<Movie> findAllWithGenres(Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.id = :id ")
    @EntityGraph(attributePaths = "genreMovie.genre")
    Optional<Movie> findByIdWithGenres(Long id);
}
