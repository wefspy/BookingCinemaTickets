package ru.alexandr.BookingCinemaTickets.application.service;

import io.jsonwebtoken.MalformedJwtException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.BookingCinemaTickets.application.dto.LoginRequestDto;
import ru.alexandr.BookingCinemaTickets.application.dto.LoginResponseDto;
import ru.alexandr.BookingCinemaTickets.application.dto.RegisterDto;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.parser.AccessTokenParser;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.parser.RefreshTokenParser;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.PostgreSQLTestContainer;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.TestActiveProfile;

@TestActiveProfile
@SpringBootTest
@Transactional
@PostgreSQLTestContainer
public class AuthServiceIntegrationTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private AccessTokenParser accessTokenParser;
    @Autowired
    private RefreshTokenParser refreshTokenParser;

    @Autowired
    private RegistrationService registrationService;

    private RegisterDto registerDto;
    private UserProfileInfoDto userProfileDto;

    @BeforeEach
    void setup() {
        registerDto = new RegisterDto(
                "TEST-USERNAME-1",
                "security-password",
                null,
                null
        );
        userProfileDto = registrationService.register(registerDto);
    }

    @Test
    void login_ShouldReturnLoginResponseDto_WhenGivenCorrectCredentials() {
        LoginRequestDto loginRequestDto = new LoginRequestDto(registerDto.username(), registerDto.password());

        LoginResponseDto actualDto = authService.login(loginRequestDto);

        Assertions.assertThat(accessTokenParser.isValid(actualDto.accessToken())).isTrue();
        Assertions.assertThat(refreshTokenParser.isValid(actualDto.refreshToken())).isTrue();
    }

    @Test
    void login_ShouldThrowBadCredentialsException_WhenGivenIncorrectPassword() {
        LoginRequestDto loginRequestDto = new LoginRequestDto(registerDto.username(), registerDto.password() + "INCORRECT");

        Assertions.assertThatThrownBy(() -> authService.login(loginRequestDto))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void refresh_ShouldReturnNewLoginResponseDto_WhenGivenCorrectToken() {
        LoginRequestDto loginRequestDto = new LoginRequestDto(registerDto.username(), registerDto.password());
        LoginResponseDto tokens = authService.login(loginRequestDto);

        LoginResponseDto actualDto = authService.refresh(tokens.refreshToken());

        Assertions.assertThat(accessTokenParser.isValid(actualDto.accessToken())).isTrue();
        Assertions.assertThat(refreshTokenParser.isValid(actualDto.refreshToken())).isTrue();
    }

    @Test
    void refresh_ShouldThrowException_WhenGivenIncorrectToken() {
        String invalidRefreshToken = "INCORRECT";

        Assertions.assertThatThrownBy(() -> authService.refresh(invalidRefreshToken))
                .isInstanceOf(MalformedJwtException.class);
    }
}
