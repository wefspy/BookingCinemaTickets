package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.RoleUser;

public interface RoleUserRepository extends CrudRepository<RoleUser, Long> {
}
