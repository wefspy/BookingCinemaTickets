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
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.PostgreSQLTestContainer;
import ru.alexandr.BookingCinemaTickets.testUtils.configuration.PostgresTestContainerConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@PostgreSQLTestContainer
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

    private User user1;
    private User user2;
    private UserInfo userInfo1;
    private UserInfo userInfo2;
    private Role role1;
    private Role role2;
    private RoleUser roleUser1;
    private RoleUser roleUser2;

    @BeforeEach
    void setUp() {
        role1 = roleRepository.save(new Role("TEST-ROLE-USER-1"));
        role2 = roleRepository.save(new Role("TEST-ROLE-USER-2"));

        user1 = userRepository.save(new User("TEST-USERNAME-1", "password1"));
        user2 = userRepository.save(new User("TEST-USERNAME-2", "password2"));

        userInfo1 = userInfoRepository.save(new UserInfo(user1, LocalDateTime.now()));
        userInfo2 = userInfoRepository.save(new UserInfo(user2, LocalDateTime.now()));

        roleUser1 = roleUserRepository.save(new RoleUser(user1, role1));
        roleUser2 = roleUserRepository.save(new RoleUser(user2, role2));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenGivenExistingUsername() {
        Optional<User> user = userRepository.findByUsername(user2.getUsername());

        assertThat(user).isPresent();
    }

    @Test
    void findByUsername_ShouldReturnEmptyOptional_WhenGivenNotExistingUsername() {
        Optional<User> user = userRepository.findByUsername("NOT-EXISTING-USERNAME-1");

        assertThat(user).isEmpty();
    }

    @Test
    void findByUsernameWithRoles_ShouldReturnUser_WhenGivenExistingUsername() {
        Optional<User> optionalUser = userRepository.findByUsernameWithRoles(user2.getUsername());

        assertThat(optionalUser).isPresent();
        User user = optionalUser.get();

        fieldRolesIsNotNull(user);
    }

    @Test
    void findByUsernameWithRoles_ShouldReturnEmptyOptional_WhenGivenNotExistingUsername() {
        Optional<User> optionalUser = userRepository.findByUsernameWithRoles("NOT-EXISTING-USERNAME-1");

        assertThat(optionalUser).isEmpty();
    }

    @Test
    void existsByUsername_ShouldReturnTrue_WhenUserExists() {
        Boolean exists = userRepository.existsByUsername(user2.getUsername());

        assertThat(exists).isTrue();
    }

    @Test
    void existsByUsername_ShouldReturnFalse_WhenUserDoesNotExist() {
        Boolean exists = userRepository.existsByUsername("NOT-EXISTING-USERNAME-1");

        assertThat(exists).isFalse();
    }

    @Test
    void findByRoleName_ShouldReturnUsersWithGivenRole() {
        List<User> admins = userRepository.findByRoleName(role1.getName());

        assertThat(admins).hasSize(1)
                .extracting(User::getUsername)
                .containsExactly(user1.getUsername());
    }

    @Test
    void findByRoleName_ShouldReturnEmptyList_WhenNoUsersWithRole() {
        List<User> moderators = userRepository.findByRoleName("NOT-EXISTING-ROLE-1");

        assertThat(moderators).isEmpty();
    }

    @Test
    void findAllWithInfoAndRoles_ShouldReturnAllUsersWithInfoAndGivenRole() {
        Page<User> userPage = userRepository.findAllWithInfoAndRoles(PageRequest.of(0, 10));

        assertThat(userPage).isNotNull();
        userPage.forEach(this::fieldsUserInfoAndRolesIsNotNull);
    }

    @Test
    void findAllWithInfoAndRoles_ShouldReturnAllUsersWithInfoAndGivenRole_WhenUsersHaveMultipleRoles() {
        Role roleManager = new Role("TEST-ROLE-USER-3");
        Role roleSupport = new Role("TEST-ROLE-USER-4");
        roleRepository.saveAll(List.of(roleManager, roleSupport));

        RoleUser roleUserAdminManager = new RoleUser(user1, roleManager);
        RoleUser roleUserUserSupport = new RoleUser(user2, roleSupport);
        roleUserRepository.saveAll(List.of(roleUserAdminManager, roleUserUserSupport));

        Page<User> userPage = userRepository.findAllWithInfoAndRoles(PageRequest.of(0, 10));

        assertThat(userPage).isNotNull();
        assertThat(userPage.getContent()).isNotEmpty();

        userPage.forEach(this::fieldsUserInfoAndRolesIsNotNull);
    }

    @Test
    void findByIdWithRoles_ShouldReturnUserWithRoles_WhenGivenExistingId() {
        Optional<User> optionalUser = userRepository.findByIdWithRoles(user1.getId());

        assertThat(optionalUser).isPresent();
        User user = optionalUser.get();

        fieldRolesIsNotNull(user);
    }

    @Test
    void findByIdWithRoles_ShouldReturnEmptyOptional_WhenGivenNotExistingId() {
        Optional<User> optionalUser = userRepository.findByIdWithRoles(Long.MIN_VALUE);

        assertThat(optionalUser).isEmpty();
    }

    @Test
    void findAllWithInfoAndRoles_ShouldReturnAllUsersWithInfoAndGivenRole_WhenUsersHaveSameRoles() {
        Role roleManager = roleRepository.save(new Role("TEST-ROLE-USER-3"));

        RoleUser roleUserAdminManager = new RoleUser(user1, roleManager);
        RoleUser roleUserUserSupport = new RoleUser(user2, roleManager);
        roleUserRepository.saveAll(List.of(roleUserAdminManager, roleUserUserSupport));

        Page<User> userPage = userRepository.findAllWithInfoAndRoles(PageRequest.of(0, 10));

        assertThat(userPage).isNotNull();
        assertThat(userPage.getContent()).isNotEmpty();

        userPage.forEach(this::fieldsUserInfoAndRolesIsNotNull);
    }

    @Test
    void findByIdWithInfoAndRoles_ShouldReturnUserWithInfoAndGivenRole() {
        Optional<User> userOptional = userRepository.findByIdWithInfoAndRoles(user1.getId());

        assertThat(userOptional).isPresent();
        User user = userOptional.get();

        fieldsUserInfoAndRolesIsNotNull(user);
    }

    void fieldRolesIsNotNull(User user) {
        assertThat(user.getRoleUser()).isNotEmpty();
        user.getRoleUser().forEach(roleUser -> {
            assertThat(roleUser.getRole()).isNotNull();
        });
    }

    void fieldUserInfoIsNotNull(User user) {
        assertThat(user.getUserInfo()).isNotNull();
    }

    void fieldsUserInfoAndRolesIsNotNull(User user) {
        fieldRolesIsNotNull(user);
        fieldUserInfoIsNotNull(user);
    }
}