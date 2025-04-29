package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.alexandr.BookingCinemaTickets.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Query("SELECT u " +
            "FROM User u " +
            "JOIN u.roleUser ru " +
            "JOIN ru.role r " +
            "WHERE r.name = :roleName")
    List<User> findByRoleName(String roleName);

    @EntityGraph(attributePaths = {
            "userInfo",
            "roleUser.role"
    })
    @Query("SELECT u " +
            "FROM User u ")
    Page<User> findAllWithInfoAndRoles(Pageable pageable);

    @EntityGraph(attributePaths = {
            "userInfo",
            "roleUser.role"
    })
    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.id = :id ")
    Optional<User> findByIdWithInfoAndRoles(@Param("id") Long id);
}
