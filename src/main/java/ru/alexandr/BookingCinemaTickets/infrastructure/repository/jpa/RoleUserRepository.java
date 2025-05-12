package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.model.RoleUser;

import java.util.List;

public interface RoleUserRepository extends CrudRepository<RoleUser, Long> {
    @Query("SELECT ru " +
            "FROM RoleUser ru " +
            "JOIN FETCH ru.user " +
            "JOIN FETCH ru.role " +
            "WHERE ru.user.id = :userId ")
    List<RoleUser> findByUserId(Long userId);
}
