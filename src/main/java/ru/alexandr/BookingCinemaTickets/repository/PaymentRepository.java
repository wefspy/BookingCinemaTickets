package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.Payment;

import java.util.UUID;

public interface PaymentRepository extends CrudRepository<Payment, UUID> {
}
