package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;

    private Role roleUser;
    private Role roleAdmin;
    private Collection<Role> roles;

    @BeforeEach
    void setUp() {
        roleUser = roleRepository.save(new Role("ROLE_USER"));
        roleAdmin = roleRepository.save(new Role("ROLE_ADMIN"));
        roles = List.of(roleUser, roleAdmin);
    }

    @Test
    void findByNameIn_ShouldReturnCollection_WhenNameExists() {
        List<String> roleStrings = roles.stream()
                .map(Role::getName)
                .toList();

        Collection<Role> actual = roleRepository.findByNameIn(roleStrings);

        assertThat(actual).isNotNull()
                .isNotEmpty()
                .containsAll(roles)
                .hasSize(roles.size());
    }

    @Test
    void findByNameIn_ShouldReturnEmptyCollection_WhenNameNotExists() {
        Collection<Role> actual = roleRepository.findByNameIn(List.of("1", "2"));

        assertThat(actual).isNotNull()
                .isEmpty();
    }

    @Test
    void findAllByNameWithRoleUser_ShouldReturnCollection_WhenIdExists() {
        Collection<String> existNames = roles.stream()
                .map(Role::getName)
                .toList();

        Collection<Role> actual = roleRepository.findAllByNameWithRoleUser(existNames);

        assertThat(actual).isNotNull()
                .isNotEmpty()
                .containsAll(roles)
                .hasSize(roles.size());
    }

    @Test
    void findAllByNameWithRoleUser_ShouldReturnEmptyCollection_WhenIdNotExists() {
        Collection<String> notExistsNames = List.of("Long.MIN_VALUE");

        Collection<Role> actual = roleRepository.findAllByNameWithRoleUser(notExistsNames);

        assertThat(actual).isNotNull()
                .isEmpty();
    }
}