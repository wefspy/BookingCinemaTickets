package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.alexandr.BookingCinemaTickets.domain.RoleUser;

import java.util.List;

@RepositoryRestResource
public interface RoleUserRepository extends CrudRepository<RoleUser, Long> {
    @Query("SELECT ru " +
            "FROM RoleUser ru " +
            "JOIN FETCH ru.user " +
            "JOIN FETCH ru.role " +
            "WHERE ru.user.id = :userId ")
    List<RoleUser> findByUserId(@Param("userId") Long userId);
}
