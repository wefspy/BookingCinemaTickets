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
import ru.alexandr.BookingCinemaTickets.dto.RoleDto;
import ru.alexandr.BookingCinemaTickets.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.dto.UserRegisterDto;
import ru.alexandr.BookingCinemaTickets.exception.UsernameAlreadyTakenException;
import ru.alexandr.BookingCinemaTickets.repository.RoleRepository;
import ru.alexandr.BookingCinemaTickets.repository.RoleUserRepository;
import ru.alexandr.BookingCinemaTickets.repository.UserInfoRepository;
import ru.alexandr.BookingCinemaTickets.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

@SpringBootTest
class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleUserRepository roleUserRepository;
    @Autowired
    private DateTimeConfig dateTimeConfig;

    private UserRegisterDto userRegisterDto;
    private Role roleAdmin;

    @BeforeEach
    void setUp() {
        roleAdmin = new Role("ADMIN");

        roleRepository.save(roleAdmin);

        userRegisterDto = new UserRegisterDto(
                "testuser",
                "securepass",
                Set.of(roleAdmin.getId()),
                "test@example.com",
                "1234567890",
                LocalDateTime.now()
        );
    }

    @AfterEach
    void tearDown() {
        roleUserRepository.deleteAll();

        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void createUserWithInfo_ShouldSaveUserWithRolesAndUserInfo() {
        userService.createUserWithInfo(userRegisterDto);

        Optional<User> savedUser = userRepository.findByUsername(userRegisterDto.username());
        Optional<UserInfo> savedUserInfo = userInfoRepository.findByEmail(userRegisterDto.email());

        assertThat(savedUser).isPresent();
        assertThat(savedUserInfo).isPresent();

        List<RoleUser> roleUsers = roleUserRepository.findByUserId(savedUser.get().getId());
        assertThat(roleUsers).isNotEmpty();
    }

    @Test
    void createUserWithInfo_ShouldReturnCorrectUserProfileInfoDto() {
        UserProfileInfoDto userProfileInfoDto = userService.createUserWithInfo(userRegisterDto);

        assertThat(userProfileInfoDto).isNotNull();

        assertThat(userProfileInfoDto.userName()).isEqualTo(userRegisterDto.username());
        assertThat(userProfileInfoDto.roles())
                .hasSize(1)
                .extracting(RoleDto::id)
                .contains(roleAdmin.getId());
        assertThat(userProfileInfoDto.email()).isEqualTo(userRegisterDto.email());
        assertThat(userProfileInfoDto.phoneNumber()).isEqualTo(userRegisterDto.phoneNumber());
        assertThat(userProfileInfoDto.createdAt())
                .isEqualTo(userRegisterDto.createdAt().format(dateTimeConfig.getFormatter()));
    }

    @Test
    void createUserWithInfo_ShouldSaveCorrectUser() {
        userService.createUserWithInfo(userRegisterDto);

        Optional<User> userOptional = userRepository.findByUsername(userRegisterDto.username());

        assertThat(userOptional).isPresent();

        User user = userOptional.get();

        assertThat(user.getUsername()).isEqualTo(userRegisterDto.username());
        assertThat(user.equalsPassword(userRegisterDto.password())).isTrue();
    }

    @Test
    void createUserWithInfo_ShouldSaveCorrectUserInfo() {
        userService.createUserWithInfo(userRegisterDto);

        Optional<UserInfo> userInfoOptional = userInfoRepository.findByEmail(userRegisterDto.email());

        assertThat(userInfoOptional).isPresent();

        UserInfo userInfo = userInfoOptional.get();

        assertThat(userInfo.getEmail()).isEqualTo(userRegisterDto.email());
        assertThat(userInfo.getPhoneNumber()).isEqualTo(userRegisterDto.phoneNumber());
    }

    @Test
    void createUserWithInfo_ShouldSaveCorrectRoles() {
        userService.createUserWithInfo(userRegisterDto);

        Optional<User> savedUser = userRepository.findByUsername(userRegisterDto.username());
        assertThat(savedUser).isPresent();

        List<RoleUser> roleUsers = roleUserRepository.findByUserId(savedUser.get().getId());

        assertThat(roleUsers)
                .extracting(roleUser -> roleUser.getRole().getId())
                .containsAll(userRegisterDto.roleIds())
                .hasSize(userRegisterDto.roleIds().size());
    }

    @Test
    void createUserWithInfo_ShouldRollbackTransaction_OnConstraintViolation() {
        userService.createUserWithInfo(userRegisterDto);

        UserRegisterDto dtoWithDuplicateUserName = new UserRegisterDto(
                userRegisterDto.username(),
                "password2",
                Set.of(roleAdmin.getId()),
                "test2@example.com",
                "222222",
                LocalDateTime.now()
        );

        assertThatException().isThrownBy(() -> userService.createUserWithInfo(dtoWithDuplicateUserName))
                .isInstanceOf(UsernameAlreadyTakenException.class);

        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(userInfoRepository.count()).isEqualTo(1);

        Optional<User> optionalUser = userRepository.findByUsername(userRegisterDto.username());
        assertThat(optionalUser).isPresent();

        UserInfo userInfo = optionalUser.get().getUserInfo();
        assertThat(userInfo.getEmail()).isEqualTo(userRegisterDto.email());
        assertThat(userInfo.getPhoneNumber()).isEqualTo(userRegisterDto.phoneNumber());
    }
}