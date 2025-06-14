package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.model.Payment;

public interface PaymentRepository extends CrudRepository<Payment, Long> {
}
