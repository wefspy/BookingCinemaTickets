package ru.alexandr.BookingCinemaTickets.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alexandr.BookingCinemaTickets.config.DateTimeConfig;
import ru.alexandr.BookingCinemaTickets.domain.Role;
import ru.alexandr.BookingCinemaTickets.domain.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.User;
import ru.alexandr.BookingCinemaTickets.domain.UserInfo;
import ru.alexandr.BookingCinemaTickets.dto.UserProfileInfoDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileInfoMapperTest {

    @InjectMocks
    private UserProfileInfoMapper userProfileInfoMapper;

    @Mock
    private DateTimeConfig dateTimeConfig;

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

        role = new Role("user");

        roleUser = new RoleUser(
                user,
                role
        );
    }

    @Test
    void toDto_shouldMapCorrectly() {
        when(dateTimeConfig.getFormatter()).thenReturn(DateTimeFormatter.ISO_DATE_TIME);

        UserProfileInfoDto result = userProfileInfoMapper.toDto(
                user,
                userInfo,
                Set.of(role)
        );

        assertThat(result).isNotNull();

        assertThat(result.userName()).isEqualTo(user.getUsername());
        assertThat(result.roles()).isEqualTo(Set.of(role.getName()));
        assertThat(result.email()).isEqualTo(userInfo.getEmail());
        assertThat(result.phoneNumber()).isEqualTo(userInfo.getPhoneNumber());
        assertThat(result.createdAt()).isEqualTo(userInfo.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME));
    }

    @Test
    void toDto_shouldReturnException_whenGetNullArgument() {
        assertThatThrownBy(() -> userProfileInfoMapper.toDto(
                user,
                userInfo,
                null
        )).isInstanceOf(NullPointerException.class);
    }
}