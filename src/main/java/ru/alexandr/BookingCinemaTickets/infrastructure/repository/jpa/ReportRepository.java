package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexandr.BookingCinemaTickets.domain.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
