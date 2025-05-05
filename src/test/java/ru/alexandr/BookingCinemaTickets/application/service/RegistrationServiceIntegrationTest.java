package ru.alexandr.BookingCinemaTickets.application.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.alexandr.BookingCinemaTickets.application.dto.RegisterDto;
import ru.alexandr.BookingCinemaTickets.application.dto.RoleDto;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.application.exception.UsernameAlreadyTakenException;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.domain.model.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.domain.model.UserInfo;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.RoleRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.RoleUserRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserInfoRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.RoleEnum;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

@SpringBootTest
class RegistrationServiceIntegrationTest {
    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleUserRepository roleUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private RegisterDto registerDto;

    @BeforeEach
    void setUp() {
        roleRepository.save(new Role("ROLE_USER"));

        registerDto = new RegisterDto(
                "testuser",
                "securepass",
                "test@example.com",
                "1234567890"
        );
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void createUserWithInfo_ShouldSaveUserWithRolesAndInfo() {
        registrationService.register(registerDto);

        Optional<User> savedUser = userRepository.findByUsername(registerDto.username());
        Optional<UserInfo> savedUserInfo = userInfoRepository.findByEmail(registerDto.email());

        assertThat(savedUser).isPresent();
        assertThat(savedUserInfo).isPresent();

        List<RoleUser> roleUsers = roleUserRepository.findByUserId(savedUser.get().getId());
        assertThat(roleUsers).isNotEmpty();
    }

    @Test
    void createUserWithInfo_ShouldReturnCorrectProfileInfoDto() {
        UserProfileInfoDto userProfileInfoDto = registrationService.register(registerDto);

        assertThat(userProfileInfoDto).isNotNull();

        assertThat(userProfileInfoDto.userName()).isEqualTo(registerDto.username());
        assertThat(userProfileInfoDto.roles())
                .hasSize(1)
                .extracting(RoleDto::name)
                .contains(RoleEnum.USER.getAuthority());
        assertThat(userProfileInfoDto.email()).isEqualTo(registerDto.email());
        assertThat(userProfileInfoDto.phoneNumber()).isEqualTo(registerDto.phoneNumber());
        assertThat(userProfileInfoDto.createdAt())
                .isNotNull();
    }

    @Test
    void createUserWithInfo_ShouldSaveCorrect() {
        registrationService.register(registerDto);

        Optional<User> userOptional = userRepository.findByUsername(registerDto.username());

        assertThat(userOptional).isPresent();

        User user = userOptional.get();

        assertThat(user.getUsername()).isEqualTo(registerDto.username());
        assertThat(passwordEncoder.matches(registerDto.password(), user.getPasswordHash())).isTrue();
    }

    @Test
    void createUserWithInfo_ShouldSaveCorrectInfo() {
        registrationService.register(registerDto);

        Optional<UserInfo> userInfoOptional = userInfoRepository.findByEmail(registerDto.email());

        assertThat(userInfoOptional).isPresent();

        UserInfo userInfo = userInfoOptional.get();

        assertThat(userInfo.getEmail()).isEqualTo(registerDto.email());
        assertThat(userInfo.getPhoneNumber()).isEqualTo(registerDto.phoneNumber());
    }

    @Test
    void register_ShouldSaveCorrectRoles() {
        registrationService.register(registerDto);

        Optional<User> savedUser = userRepository.findByUsername(registerDto.username());
        assertThat(savedUser).isPresent();

        List<RoleUser> roleUsers = roleUserRepository.findByUserId(savedUser.get().getId());

        assertThat(roleUsers).isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void register_ShouldRollbackTransaction_OnConstraintViolation() {
        registrationService.register(registerDto);

        RegisterDto dtoWithDuplicateUserName = new RegisterDto(
                registerDto.username(),
                "password2",
                "test2@example.com",
                "222222"
        );

        assertThatException().isThrownBy(() -> registrationService.register(dtoWithDuplicateUserName))
                .isInstanceOf(UsernameAlreadyTakenException.class);

        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(userInfoRepository.count()).isEqualTo(1);

        Optional<User> optionalUser = userRepository.findByUsername(registerDto.username());
        assertThat(optionalUser).isPresent();

        UserInfo userInfo = optionalUser.get().getUserInfo();
        assertThat(userInfo.getEmail()).isEqualTo(registerDto.email());
        assertThat(userInfo.getPhoneNumber()).isEqualTo(registerDto.phoneNumber());
    }
}