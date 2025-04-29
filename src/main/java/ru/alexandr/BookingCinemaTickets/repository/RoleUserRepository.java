package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.alexandr.BookingCinemaTickets.domain.RoleUser;

import java.util.List;

public interface RoleUserRepository extends CrudRepository<RoleUser, Long> {
    @Query("SELECT ru " +
            "FROM RoleUser ru " +
            "JOIN FETCH ru.user " +
            "JOIN FETCH ru.role " +
            "WHERE ru.user.id = :userId ")
    List<RoleUser> findByUserId(@Param("userId") Long userId);
}
