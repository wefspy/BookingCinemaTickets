package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.Role;

import java.util.UUID;

public interface RoleRepository extends CrudRepository<Role, UUID> {
}
