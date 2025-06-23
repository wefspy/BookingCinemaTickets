package ru.alexandr.BookingCinemaTickets.application.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.BookingCinemaTickets.application.dto.RegisterDto;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.application.exception.UserNotFoundException;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.PostgreSQLTestContainer;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.TestActiveProfile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestActiveProfile
@SpringBootTest
@Transactional
@PostgreSQLTestContainer
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private EntityManager entityManager;


    private final RegisterDto registerDto = new RegisterDto(
            "testuser",
            "securepass",
            "test@example.com",
            "1234567890"
    );
    private UserProfileInfoDto userProfileInfoDto;

    @BeforeEach
    void setUp() {
        userProfileInfoDto = registrationService.register(registerDto);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void getUserProfileInfo_ShouldReturnUserProfileInfo_whenGetCorrectUserId() {
        UserProfileInfoDto result = userService.getUserProfileInfo(userProfileInfoDto.userId());

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