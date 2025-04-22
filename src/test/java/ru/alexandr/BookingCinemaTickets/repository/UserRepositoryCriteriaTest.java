package ru.alexandr.BookingCinemaTickets.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.alexandr.BookingCinemaTickets.domain.Role;
import ru.alexandr.BookingCinemaTickets.domain.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(UserRepositoryCriteria.class)
class UserRepositoryCriteriaTest {

    @Autowired
    private UserRepositoryCriteria userRepositoryCriteria;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleUserRepository roleUserRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User userAdmin;
    private User userUser;
    private Role roleAdmin;
    private Role roleUser;

    @BeforeEach
    void setUp() {
        assertThat(userRepository.count()).isZero();
        assertThat(roleRepository.count()).isZero();
        assertThat(roleUserRepository.count()).isZero();

        roleAdmin = roleRepository.save(new Role("ADMIN"));
        roleUser = roleRepository.save(new Role("USER"));

        userAdmin = userRepository.save(new User("admin1", "password1"));
        userUser = userRepository.save(new User("user1", "password2"));

        roleUserRepository.save(new RoleUser(userAdmin, roleAdmin));
        roleUserRepository.save(new RoleUser(userUser, roleUser));

        entityManager.flush();
    }

    @Test
    void findUsersByRoleName_ShouldReturnUsersWithGivenRole() {
        List<User> admins = userRepositoryCriteria.findByRoleName(roleAdmin.getName());

        assertThat(admins).hasSize(1)
                .extracting(User::getUsername)
                .containsExactly(userAdmin.getUsername());
    }

    @Test
    void findUsersByRoleName_ShouldReturnEmptyList_WhenNoUsersWithRole() {
        List<User> moderators = userRepositoryCriteria.findByRoleName("MODERATOR");

        assertThat(moderators).isEmpty();
    }
}