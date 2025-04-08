package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.Payment;

public interface PaymentRepository extends CrudRepository<Payment, Long> {
}
