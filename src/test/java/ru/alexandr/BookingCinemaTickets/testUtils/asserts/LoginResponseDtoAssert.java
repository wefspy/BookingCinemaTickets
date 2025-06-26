package ru.alexandr.BookingCinemaTickets.testUtils.asserts;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import ru.alexandr.BookingCinemaTickets.application.dto.LoginResponseDto;

public class LoginResponseDtoAssert extends AbstractAssert<LoginResponseDtoAssert, LoginResponseDto> {
    protected LoginResponseDtoAssert(LoginResponseDto loginResponseDto) {
        super(loginResponseDto, LoginResponseDtoAssert.class);
    }

    public static LoginResponseDtoAssert assertThat(LoginResponseDto loginResponseDto) {
        return new LoginResponseDtoAssert(loginResponseDto);
    }

    public LoginResponseDtoAssert hasAccessToken(String expected) {
        Assertions.assertThat(actual.accessToken()).isEqualTo(expected);
        return this;
    }

    public LoginResponseDtoAssert hasRefreshToken(String expected) {
        Assertions.assertThat(actual.refreshToken()).isEqualTo(expected);
        return this;
    }
}
