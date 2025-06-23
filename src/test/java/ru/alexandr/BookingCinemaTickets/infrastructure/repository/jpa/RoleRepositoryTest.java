package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.PostgreSQLTestContainer;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@PostgreSQLTestContainer
class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Role roleUser;
    private Role roleAdmin;
    private Collection<Role> roles;

    @BeforeEach
    void setUp() {
        roleUser = roleRepository.save(new Role("TEST-ROLE-USER-1"));
        roleAdmin = roleRepository.save(new Role("TEST-ROLE-USER-2"));
        roles = List.of(roleUser, roleAdmin);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findByNameIn_ShouldReturnCollection_WhenNameExists() {
        List<String> roleStrings = roles.stream()
                .map(Role::getName)
                .toList();

        Collection<Role> actual = roleRepository.findByNameIn(roleStrings);

        assertThat(actual).isNotNull()
                .isNotEmpty()
                .hasSize(roles.size())
                .containsExactlyInAnyOrderElementsOf(roles);
    }

    @Test
    void findByNameIn_ShouldReturnEmptyCollection_WhenNameNotExists() {
        Collection<Role> actual = roleRepository.findByNameIn(List.of("NOT-EXISTS-ROLE-1", "NOT-EXISTS-ROLE-1"));

        assertThat(actual).isNotNull()
                .isEmpty();
    }

}