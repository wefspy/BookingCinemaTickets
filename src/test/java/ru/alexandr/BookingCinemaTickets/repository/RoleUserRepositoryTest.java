package ru.alexandr.BookingCinemaTickets.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.alexandr.BookingCinemaTickets.domain.Role;
import ru.alexandr.BookingCinemaTickets.domain.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
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
        user = new User(
                "username",
                "securityPassword"
        );
        userRepository.save(user);

        roleAdmin = new Role("ADMIN");
        roleUser = new Role("USER");
        roleRepository.saveAll(List.of(roleAdmin, roleUser));
    }

    @Test
    void findByUserId_shouldReturnRelatedRoles() {
        RoleUser roleUserAdmin = new RoleUser(user, roleAdmin);
        RoleUser roleUserUser = new RoleUser(user, roleUser);
        roleUserRepository.saveAll(List.of(roleUserAdmin, roleUserUser));

        List<RoleUser> roleUsers = roleUserRepository.findByUserId(user.getId());

        assertThat(roleUsers)
                .containsAll(List.of(roleUserAdmin, roleUserUser))
                .hasSize(2);
    }

    @Test
    void findByUserId_shouldReturnEmptyList_whenNoRelatedRoles() {
        List<RoleUser> roleUsers = roleUserRepository.findByUserId(user.getId());

        assertThat(roleUsers).isEmpty();
    }
}