package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;

import java.util.Collection;
import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {

    @EntityGraph(attributePaths = {
            "roleUser"
    })
    @Query("SELECT r " +
            "FROM Role r " +
            "WHERE r.id = :id ")
    Optional<Role> findByIdWithRoleUser(Long id);

    Optional<Role> findByName(String name);

    @EntityGraph(attributePaths = {
            "roleUser"
    })
    @Query("SELECT r " +
            "FROM Role r " +
            "WHERE r.name = :name ")
    Optional<Role> findByNameWithRoleUser(String name);

    Collection<Role> findByNameIn(Collection<String> names);

    @EntityGraph(attributePaths = {
            "roleUser"
    })
    @Query("SELECT r " +
            "FROM Role r " +
            "WHERE r.name IN :names ")
    Collection<Role> findAllByNameWithRoleUser(Collection<String> names);
}
