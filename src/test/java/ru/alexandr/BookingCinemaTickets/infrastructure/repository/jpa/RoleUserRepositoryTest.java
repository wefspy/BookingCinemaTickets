package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.domain.model.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.PostgreSQLTestContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@PostgreSQLTestContainer
class RoleUserRepositoryTest {
    @Autowired
    private RoleUserRepository roleUserRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @PersistenceContext
    private EntityManager entityManager;


    private User user;
    private Role roleAdmin;
    private Role roleUser;

    @BeforeEach
    void setUp() {
        user = new User("username", "securityPassword");
        user = userRepository.save(user);

        roleAdmin = new Role("TEST-ROLE-USER-1");
        roleUser = new Role("TEST-ROLE-USER-2");
        roleRepository.saveAll(List.of(roleAdmin, roleUser));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findByUserId_shouldReturnRelatedRoles() {
        RoleUser roleUserAdmin = new RoleUser(user, roleAdmin);
        RoleUser roleUserUser = new RoleUser(user, roleUser);
        List<RoleUser> expectedRoleUsers = List.of(roleUserAdmin, roleUserUser);
        roleUserRepository.saveAll(expectedRoleUsers);

        List<RoleUser> actualRoleUsers = roleUserRepository.findByUserId(user.getId());

        assertThat(actualRoleUsers)
                .containsExactlyInAnyOrderElementsOf(expectedRoleUsers)
                .hasSize(expectedRoleUsers.size());
    }

    @Test
    void findByUserId_shouldReturnEmptyList_whenNoRelatedRoles() {
        List<RoleUser> roleUsers = roleUserRepository.findByUserId(user.getId());

        assertThat(roleUsers).isEmpty();
    }
}