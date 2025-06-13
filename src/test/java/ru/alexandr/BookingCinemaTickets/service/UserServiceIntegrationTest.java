package ru.alexandr.BookingCinemaTickets.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.alexandr.BookingCinemaTickets.config.DateTimeConfig;
import ru.alexandr.BookingCinemaTickets.domain.Role;
import ru.alexandr.BookingCinemaTickets.domain.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.User;
import ru.alexandr.BookingCinemaTickets.domain.UserInfo;
import ru.alexandr.BookingCinemaTickets.dto.RoleDto;
import ru.alexandr.BookingCinemaTickets.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.exception.UserNotFoundException;
import ru.alexandr.BookingCinemaTickets.repository.RoleRepository;
import ru.alexandr.BookingCinemaTickets.repository.RoleUserRepository;
import ru.alexandr.BookingCinemaTickets.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private DateTimeConfig dateTimeConfig;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleUserRepository roleUserRepository;

    private User user;
    private UserInfo userInfo;
    private Role role;
    private RoleUser roleUser;
    private UserProfileInfoDto userProfileInfoDto;

    @BeforeEach
    void setUp() {
        user = new User(
                "username",
                "password"
        );

        userInfo = new UserInfo(
                user,
                LocalDateTime.now()
        );
        userRepository.save(user);

        role = new Role("user");
        roleRepository.save(role);

        roleUser = new RoleUser(
                user,
                role
        );
        roleUserRepository.save(roleUser);

        userProfileInfoDto = new UserProfileInfoDto(
                user.getId(),
                user.getUsername(),
                Set.of(new RoleDto(role.getId(), role.getName())),
                userInfo.getEmail(),
                userInfo.getPhoneNumber(),
                userInfo.getCreatedAt().format(dateTimeConfig.getFormatter())
        );
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleUserRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void getUserProfileInfo_ShouldReturnUserProfileInfo_whenGetCorrectUserId() {
        UserProfileInfoDto result = userService.getUserProfileInfo(user.getId());

        assertThat(result).isEqualTo(userProfileInfoDto);
    }

    @Test
    void getUserProfileInfo_ShouldReturnUserNotFoundException_whenGetNotExistUserId() {
        assertThatThrownBy(() -> userService.getUserProfileInfo(Long.MIN_VALUE))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getUserProfileInfoPage_ShouldReturnUserProfileInfoPage() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<UserProfileInfoDto> result = userService.getUserProfileInfoPage(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(userProfileInfoDto);
    }
}