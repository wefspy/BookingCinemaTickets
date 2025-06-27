package ru.alexandr.BookingCinemaTickets.testUtils.asserts;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.springframework.security.core.GrantedAuthority;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.domain.model.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.UserDetailsImpl;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.dto.AccessTokenInput;

import java.util.List;

public class AccessTokenInputAssert extends AbstractAssert<AccessTokenInputAssert, AccessTokenInput> {
    protected AccessTokenInputAssert(AccessTokenInput accessTokenInput) {
        super(accessTokenInput, AccessTokenInputAssert.class);
    }

    public static AccessTokenInputAssert assertThat(AccessTokenInput accessTokenInput) {
        return new AccessTokenInputAssert(accessTokenInput);
    }

    public AccessTokenInputAssert isEqualTo(UserDetailsImpl user) {
        idIsEqualTo(user.getId());
        usernameIsEqualTo(user.getUsername());

        List<String> roleNames = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        Assertions.assertThat(actual.roles()).containsExactlyInAnyOrderElementsOf(roleNames);

        return this;
    }

    public AccessTokenInputAssert isEqualTo(User userWithRoles) {
        idIsEqualTo(userWithRoles.getId());
        usernameIsEqualTo(userWithRoles.getUsername());

        List<String> roleNames = userWithRoles.getRoleUser().stream()
                .map(RoleUser::getRole)
                .map(Role::getName)
                .toList();
        Assertions.assertThat(actual.roles()).containsExactlyInAnyOrderElementsOf(roleNames);


        return this;
    }

    public AccessTokenInputAssert idIsEqualTo(Long id) {
        Assertions.assertThat(actual.userId()).isEqualTo(id);
        return this;
    }

    public AccessTokenInputAssert usernameIsEqualTo(String username) {
        Assertions.assertThat(actual.username()).isEqualTo(username);
        return this;
    }

}
