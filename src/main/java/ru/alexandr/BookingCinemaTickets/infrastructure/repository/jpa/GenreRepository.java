package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
