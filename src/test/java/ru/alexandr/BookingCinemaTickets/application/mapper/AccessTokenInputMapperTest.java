package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.domain.model.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.UserDetailsImpl;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.dto.AccessTokenInput;
import ru.alexandr.BookingCinemaTickets.testUtils.asserts.AccessTokenInputAssert;
import ru.alexandr.BookingCinemaTickets.testUtils.factory.TestEntityBuilder;

import java.util.List;

class AccessTokenInputMapperTest {

    private final AccessTokenInputMapper mapper = new AccessTokenInputMapper();

    @Test
    void from_ShouldReturnAccessTokenInput_WhenGivenUserDetailsImpl() {
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "username",
                "security-hash",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        AccessTokenInput actualInput = mapper.from(userDetails);

        AccessTokenInputAssert.assertThat(actualInput).isEqualTo(userDetails);
    }

    @Test
    void from_ShouldReturnAccessTokenInput_WhenGivenUserWithRoles() {
        User user = TestEntityBuilder.user(1L, "username", "security-hash");
        Role role = TestEntityBuilder.role(1L, "USER");
        RoleUser roleUser = TestEntityBuilder.roleUser(1L, user, role);
        user.getRoleUser().add(roleUser);

        AccessTokenInput actualInput = mapper.from(user);

        AccessTokenInputAssert.assertThat(actualInput).isEqualTo(user);
    }

    @Test
    void from_ShouldThrowNullPointerException_WhenGivenUserWithoutRoles() {
        User user = TestEntityBuilder.user(1L, "username", "security-hash");
        RoleUser roleUser = TestEntityBuilder.roleUser(1L, user, null);
        user.getRoleUser().add(roleUser);

        Assertions.assertThatThrownBy(() -> mapper.from(user)).isInstanceOf(NullPointerException.class);
    }
}