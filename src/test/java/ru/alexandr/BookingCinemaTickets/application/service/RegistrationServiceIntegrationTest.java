package ru.alexandr.BookingCinemaTickets.application.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.BookingCinemaTickets.application.dto.RegisterDto;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.application.exception.UsernameAlreadyTakenException;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.domain.model.UserInfo;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.RoleRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserInfoRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserRepository;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.PostgreSQLTestContainer;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.TestActiveProfile;
import ru.alexandr.BookingCinemaTickets.testUtils.asserts.UserProfileInfoDtoAssert;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;


@TestActiveProfile
@SpringBootTest
@Transactional
@PostgreSQLTestContainer
class RegistrationServiceIntegrationTest {
    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private RoleRepository roleRepository;

    private final RegisterDto registerDto = new RegisterDto(
            "testuser",
            "securepass",
            "test@example.com",
            "1234567890"
    );

    @Test
    void register_ShouldSaveUserWithRolesAndInfo() {
        registrationService.register(registerDto);

        Optional<User> optionalUser = userRepository.findByUsername(registerDto.username());
        Optional<UserInfo> optionalUserInfo = userInfoRepository.findByEmail(registerDto.email());

        Assertions.assertThat(optionalUser).isPresent();
        Assertions.assertThat(optionalUserInfo).isPresent();

        User user = optionalUser.get();
        Collection<Role> roles = roleRepository.findAllByRoleUserUserId(user.getId());
        Assertions.assertThat(roles).isNotEmpty();
    }

    @Test
    void register_ShouldReturnCorrectProfileInfoDto() {
        LocalDateTime startRegisterTime = LocalDateTime.now();
        UserProfileInfoDto userProfileInfoDto = registrationService.register(registerDto);
        LocalDateTime endRegisterTime = LocalDateTime.now();

        UserProfileInfoDtoAssert.assertThat(userProfileInfoDto)
                .isNotNull()
                .hasRegisterDto(registerDto, startRegisterTime, endRegisterTime)
                .hasBaseRoles();
    }

    @Test
    void register_ShouldRollbackTransaction_OnConstraintViolation() {
        UserProfileInfoDto registeredDto = registrationService.register(registerDto);

        RegisterDto dtoWithDuplicateUserName = new RegisterDto(
                registerDto.username(),
                "password2",
                "test2@example.com",
                "222222"
        );

        Assertions.assertThatException().isThrownBy(() -> registrationService.register(dtoWithDuplicateUserName))
                .isInstanceOf(UsernameAlreadyTakenException.class);

        Assertions.assertThat(userRepository.count()).isEqualTo(1);
        Assertions.assertThat(userInfoRepository.count()).isEqualTo(1);

        Optional<User> optionalSavedUser = userRepository.findByUsername(registerDto.username());
        Assertions.assertThat(optionalSavedUser).isPresent();
        Optional<UserInfo> optionalSavedUserInfo = userInfoRepository.findByEmail(registerDto.email());
        Assertions.assertThat(optionalSavedUserInfo).isPresent();

        User savedUser = optionalSavedUser.get();
        Collection<Role> assignRoles = roleRepository.findAllByRoleUserUserId(savedUser.getId());

        UserProfileInfoDtoAssert.assertThat(registeredDto)
                .isNotNull()
                .hasUser(savedUser)
                .hasUserInfo(optionalSavedUserInfo.get())
                .hasRoles(assignRoles);

    }
}