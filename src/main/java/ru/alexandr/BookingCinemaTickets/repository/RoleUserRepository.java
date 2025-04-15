package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.alexandr.BookingCinemaTickets.domain.RoleUser;

@RepositoryRestResource
public interface RoleUserRepository extends CrudRepository<RoleUser, Long> {
}
