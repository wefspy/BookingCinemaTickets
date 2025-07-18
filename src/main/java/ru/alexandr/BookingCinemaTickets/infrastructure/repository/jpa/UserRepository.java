package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.alexandr.BookingCinemaTickets.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = {
            "roleUser.role"
    })
    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.username = :username")
    Optional<User> findByUsernameWithRoles(String username);

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
            "roleUser.role"
    })
    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.id = :id")
    Optional<User> findByIdWithRoles(Long id);

    @EntityGraph(attributePaths = {
            "userInfo",
            "roleUser.role"
    })
    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.id = :id ")
    Optional<User> findByIdWithInfoAndRoles(Long id);
}
