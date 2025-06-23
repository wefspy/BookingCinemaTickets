package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alexandr.BookingCinemaTickets.application.dto.RoleDto;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.domain.model.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.domain.model.UserInfo;
import ru.alexandr.BookingCinemaTickets.testUtils.asserts.UserProfileInfoDtoAssert;
import ru.alexandr.BookingCinemaTickets.testUtils.factory.TestEntityBuilder;

import java.time.LocalDateTime;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class UserProfileInfoMapperTest {

    @InjectMocks
    private UserProfileInfoMapper userProfileInfoMapper;
    @Mock
    private RoleMapper roleMapper;

    private User user;
    private UserInfo userInfo;
    private Role role;
    private Set<Role> roles;

    @BeforeEach
    void setUp() {
        user = TestEntityBuilder.user(1L, "username", "password");
        userInfo = TestEntityBuilder.userInfo(user.getId(), user, LocalDateTime.now());
        role = TestEntityBuilder.role(1L, "user");
        roles = Set.of(role);
    }

    @Test
    void toDto_shouldMapCorrectly() {
        Mockito.when(roleMapper.toDtos(roles)).thenReturn(Set.of(new RoleDto(role.getId(), role.getName())));

        UserProfileInfoDto actualDto = userProfileInfoMapper.toDto(user, userInfo, roles);

        UserProfileInfoDtoAssert.assertThat(actualDto)
                .isNotNull()
                .hasUser(user)
                .hasRoles(roles)
                .hasUserInfo(userInfo);
    }

    @Test
    void toDto_shouldReturnException_whenGetNullArgument() {
        Assertions.assertThatThrownBy(() -> userProfileInfoMapper.toDto(user, null, roles))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void toDto_shouldMapCorrectly_whenGetUserWithInfoAndRoles() {
        user.setUserInfo(userInfo);
        RoleUser roleUser = TestEntityBuilder.roleUser(1L, user, role);
        user.getRoleUser().add(roleUser);

        Mockito.when(roleMapper.toDtos(roles)).thenReturn(Set.of(new RoleDto(role.getId(), role.getName())));

        UserProfileInfoDto actualDto = userProfileInfoMapper.toDto(user);

        UserProfileInfoDtoAssert.assertThat(actualDto)
                .isNotNull()
                .hasUser(user)
                .hasRoles(roles)
                .hasUserInfo(userInfo);
    }
}