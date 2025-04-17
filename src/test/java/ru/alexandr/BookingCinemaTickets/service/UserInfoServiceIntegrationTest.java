package ru.alexandr.BookingCinemaTickets.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.alexandr.BookingCinemaTickets.config.DateTimeConfig;
import ru.alexandr.BookingCinemaTickets.domain.Role;
import ru.alexandr.BookingCinemaTickets.domain.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.User;
import ru.alexandr.BookingCinemaTickets.domain.UserInfo;
import ru.alexandr.BookingCinemaTickets.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.exception.UserNotFoundException;
import ru.alexandr.BookingCinemaTickets.repository.RoleRepository;
import ru.alexandr.BookingCinemaTickets.repository.RoleUserRepository;
import ru.alexandr.BookingCinemaTickets.repository.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserInfoServiceIntegrationTest {

    @Autowired
    private UserInfoService userInfoService;

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
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleUserRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void getUserProfileInfo_shouldReturnUserProfileInfo_whenGetCorrectUserId() {
        UserProfileInfoDto userProfileInfoDto = userInfoService.getUserProfileInfo(user.getId());

        assertThat(userProfileInfoDto.userName()).isEqualTo(user.getUsername());

        assertThat(userProfileInfoDto.roles()).contains(role.getName());

        assertThat(userProfileInfoDto.email()).isEqualTo(userInfo.getEmail());
        assertThat(userProfileInfoDto.phoneNumber()).isEqualTo(userInfo.getPhoneNumber());
        assertThat(userProfileInfoDto.createdAt()).isEqualTo(userInfo.getCreatedAt().format(dateTimeConfig.getFormatter()));
    }

    @Test
    void getUserProfileInfo_shouldReturnUserNotFoundException_whenGetNotExistUserId() {
        assertThatThrownBy(() -> userInfoService.getUserProfileInfo(Long.MIN_VALUE))
                .isInstanceOf(UserNotFoundException.class);
    }
}