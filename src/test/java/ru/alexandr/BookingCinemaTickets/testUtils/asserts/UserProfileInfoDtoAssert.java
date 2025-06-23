package ru.alexandr.BookingCinemaTickets.testUtils.asserts;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import ru.alexandr.BookingCinemaTickets.application.dto.RegisterDto;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.domain.model.UserInfo;

import java.time.LocalDateTime;
import java.util.Collection;

public class UserProfileInfoDtoAssert extends AbstractAssert<UserProfileInfoDtoAssert, UserProfileInfoDto> {
    protected UserProfileInfoDtoAssert(UserProfileInfoDto userProfileInfoDto) {
        super(userProfileInfoDto, UserProfileInfoDtoAssert.class);
    }

    public static UserProfileInfoDtoAssert assertThat(UserProfileInfoDto actual) {
        return new UserProfileInfoDtoAssert(actual);
    }

    public UserProfileInfoDtoAssert hasUser(User user) {
        userIdEqualsTo(user.getId());
        userNameEqualsTo(user.getUsername());

        return this;
    }

    public UserProfileInfoDtoAssert hasRoles(Collection<? extends Role> roles) {
        RoleDtoCollectionAssert.assertThat(actual.roles()).isEqualTo(roles);

        return this;
    }

    public UserProfileInfoDtoAssert hasBaseRoles() {
        RoleDtoCollectionAssert.assertThat(actual.roles()).hasBaseRoles();

        return this;
    }

    public UserProfileInfoDtoAssert hasUserInfo(UserInfo userInfo) {
        userIdEqualsTo(userInfo.getId());
        emailEqualsTo(userInfo.getEmail());
        phoneNumberEqualsTo(userInfo.getPhoneNumber());
        createdAtEqualsTo(userInfo.getCreatedAt());

        return this;
    }

    public UserProfileInfoDtoAssert hasRegisterDto(RegisterDto registerDto,
                                                   LocalDateTime timeStartRegister,
                                                   LocalDateTime timeEndRegister) {
        userNameEqualsTo(registerDto.username());
        emailEqualsTo(registerDto.email());
        phoneNumberEqualsTo(registerDto.phoneNumber());
        createdAtBetween(timeStartRegister, timeEndRegister);

        return this;
    }

    public UserProfileInfoDtoAssert userIdEqualsTo(Long expectedId) {
        Assertions.assertThat(actual.userId()).isEqualTo(expectedId);
        return this;
    }

    public UserProfileInfoDtoAssert userNameEqualsTo(String expectedUsername) {
        Assertions.assertThat(actual.userName()).isEqualTo(expectedUsername);
        return this;
    }

    public UserProfileInfoDtoAssert emailEqualsTo(String expectedEmail) {
        Assertions.assertThat(actual.email()).isEqualTo(expectedEmail);
        return this;
    }

    public UserProfileInfoDtoAssert phoneNumberEqualsTo(String expectedPhone) {
        Assertions.assertThat(actual.phoneNumber()).isEqualTo(expectedPhone);
        return this;
    }

    public UserProfileInfoDtoAssert createdAtBetween(LocalDateTime timeStartCreating, LocalDateTime timeEndCreating) {
        Assertions.assertThat(actual.createdAt())
                .isAfterOrEqualTo(timeStartCreating)
                .isBeforeOrEqualTo(timeEndCreating);

        return this;
    }

    public UserProfileInfoDtoAssert createdAtEqualsTo(LocalDateTime expectedCreatedAt) {
        createdAtBetween(expectedCreatedAt, expectedCreatedAt);
        return this;
    }
}
