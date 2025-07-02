package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.model.GenreMovie;

public interface GenreMovieRepository extends JpaRepository<GenreMovie, Long> {
}
