package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u " +
            "FROM User u " +
            "JOIN u.roleUser ru " +
            "JOIN ru.role r " +
            "WHERE r.name = :roleName")
    List<User> findByRoleName(String roleName);
}
