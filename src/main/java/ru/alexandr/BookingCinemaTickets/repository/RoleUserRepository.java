package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.RoleUser;

import java.util.UUID;

public interface RoleUserRepository extends CrudRepository<RoleUser, UUID> {
}
