package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
