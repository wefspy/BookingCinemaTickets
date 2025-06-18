package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.domain.model.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.domain.model.UserInfo;
import ru.alexandr.BookingCinemaTickets.testConfiguration.PostgresTestContainerConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PostgresTestContainerConfiguration.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleUserRepository roleUserRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User userAdmin;
    private User userUser;
    private UserInfo userInfoAdmin;
    private UserInfo userInfoUser;
    private Role roleAdmin;
    private Role roleUser;

    @BeforeEach
    void setUp() {
        roleAdmin = roleRepository.save(new Role("ADMIN"));
        roleUser = roleRepository.save(new Role("USER"));

        userAdmin = userRepository.save(new User("admin1", "password1"));
        userUser = userRepository.save(new User("user1", "password2"));

        userInfoAdmin = userInfoRepository.save(new UserInfo(userAdmin, LocalDateTime.now()));
        userInfoUser = userInfoRepository.save(new UserInfo(userUser, LocalDateTime.now()));

        roleUserRepository.save(new RoleUser(userAdmin, roleAdmin));
        roleUserRepository.save(new RoleUser(userUser, roleUser));

        entityManager.flush();
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenGivenExistingUsername() {
        Optional<User> user = userRepository.findByUsername(userUser.getUsername());

        assertThat(user).isPresent();
    }

    @Test
    void findByUsername_ShouldReturnEmptyOptional_WhenGivenNotExistingUsername() {
        Optional<User> user = userRepository.findByUsername("NotExistingUsername");

        assertThat(user).isEmpty();
    }

    @Test
    void findByUsernameWithRoles_ShouldReturnUser_WhenGivenExistingUsername() {
        Optional<User> optionalUser = userRepository.findByUsernameWithRoles(userUser.getUsername());

        assertThat(optionalUser).isPresent();

        User user = optionalUser.get();

        assertThat(user.getRoleUser())
                .isNotNull()
                .isNotEmpty()
                .allMatch(Objects::nonNull);

        assertThat(user.getRoleUser())
                .extracting(RoleUser::getRole)
                .allMatch(Objects::nonNull);
    }

    @Test
    void findByUsernameWithRoles_ShouldReturnEmptyOptional_WhenGivenNotExistingUsername() {
        Optional<User> optionalUser = userRepository.findByUsernameWithRoles("NotExistingUsername");

        assertThat(optionalUser).isEmpty();
    }

    @Test
    void existsByUsername_ShouldReturnTrue_WhenUserExists() {
        Boolean exists = userRepository.existsByUsername(userUser.getUsername());

        assertThat(exists).isTrue();
    }

    @Test
    void existsByUsername_ShouldReturnFalse_WhenUserDoesNotExist() {
        Boolean exists = userRepository.existsByUsername("notExistsUsername");

        assertThat(exists).isFalse();
    }

    @Test
    void findByRoleName_ShouldReturnUsersWithGivenRole() {
        List<User> admins = userRepository.findByRoleName(roleAdmin.getName());

        assertThat(admins).hasSize(1)
                .extracting(User::getUsername)
                .containsExactly(userAdmin.getUsername());
    }

    @Test
    void findByRoleName_ShouldReturnEmptyList_WhenNoUsersWithRole() {
        List<User> moderators = userRepository.findByRoleName("MODERATOR");

        assertThat(moderators).isEmpty();
    }

    @Test
    void findAllWithInfoAndRoles_ShouldReturnAllUsersWithInfoAndGivenRole() {
        Page<User> userPage = userRepository.findAllWithInfoAndRoles(PageRequest.of(0, 10));

        assertThat(userPage).isNotNull();
        assertThat(userPage.getContent()).isNotEmpty();

        userPage.forEach(user -> {
            assertThat(user.getUserInfo()).isNotNull();
            assertThat(user.getRoleUser()).isNotEmpty();
            user.getRoleUser().forEach(roleUser -> {
                assertThat(roleUser.getRole()).isNotNull();
            });
        });
    }

    @Test
    void findAllWithInfoAndRoles_ShouldReturnAllUsersWithInfoAndGivenRole_WhenUsersHaveMultipleRoles() {
        Role roleManager = new Role("MANAGER");
        Role roleSupport = new Role("SUPPORT");
        roleRepository.saveAll(List.of(roleManager, roleSupport));

        RoleUser roleUserAdminManager = new RoleUser(userAdmin, roleManager);
        RoleUser roleUserUserSupport = new RoleUser(userUser, roleSupport);
        roleUserRepository.saveAll(List.of(roleUserAdminManager, roleUserUserSupport));

        Page<User> userPage = userRepository.findAllWithInfoAndRoles(PageRequest.of(0, 10));

        assertThat(userPage).isNotNull();
        assertThat(userPage.getContent()).isNotEmpty();

        userPage.forEach(user -> {
            assertThat(user.getUserInfo()).isNotNull();
            assertThat(user.getRoleUser()).isNotEmpty();
            user.getRoleUser().forEach(roleUser -> {
                assertThat(roleUser.getRole()).isNotNull();
            });
        });
    }

    @Test
    void findByIdWithRoles_ShouldReturnUserWithRoles_WhenGivenExistingId() {
        Optional<User> optionalUser = userRepository.findByIdWithRoles(userAdmin.getId());

        assertThat(optionalUser).isPresent();

        User user = optionalUser.get();

        assertThat(user.getRoleUser())
                .isNotNull()
                .isNotEmpty()
                .allMatch(Objects::nonNull);

        assertThat(user.getRoleUser())
                .extracting(RoleUser::getRole)
                .allMatch(Objects::nonNull);
    }

    @Test
    void findByIdWithRoles_ShouldReturnEmptyOptional_WhenGivenNotExistingId() {
        Optional<User> optionalUser = userRepository.findByIdWithRoles(Long.MIN_VALUE);

        assertThat(optionalUser).isEmpty();
    }

    @Test
    void findAllWithInfoAndRoles_ShouldReturnAllUsersWithInfoAndGivenRole_WhenUsersHaveSameRoles() {
        Role roleManager = roleRepository.save(new Role("MANAGER"));

        RoleUser roleUserAdminManager = new RoleUser(userAdmin, roleManager);
        RoleUser roleUserUserSupport = new RoleUser(userUser, roleManager);
        roleUserRepository.saveAll(List.of(roleUserAdminManager, roleUserUserSupport));

        Page<User> userPage = userRepository.findAllWithInfoAndRoles(PageRequest.of(0, 10));

        assertThat(userPage).isNotNull();
        assertThat(userPage.getContent()).isNotEmpty();

        userPage.forEach(user -> {
            assertThat(user.getUserInfo()).isNotNull();
            assertThat(user.getRoleUser()).isNotEmpty();
            user.getRoleUser().forEach(roleUser -> {
                assertThat(roleUser.getRole()).isNotNull();
            });
        });
    }

    @Test
    void findByIdWithInfoAndRoles_ShouldReturnUserWithInfoAndGivenRole() {
        Optional<User> userOptional = userRepository.findByIdWithInfoAndRoles(userAdmin.getId());

        assertThat(userOptional).isPresent();

        User user = userOptional.get();

        assertThat(user.getUserInfo()).isNotNull();
        assertThat(user.getRoleUser()).isNotEmpty();
        user.getRoleUser().forEach(roleUser -> {
            assertThat(roleUser.getRole()).isNotNull();
        });
    }
}