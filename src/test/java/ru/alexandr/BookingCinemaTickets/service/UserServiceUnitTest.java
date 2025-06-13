package ru.alexandr.BookingCinemaTickets.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.alexandr.BookingCinemaTickets.domain.Role;
import ru.alexandr.BookingCinemaTickets.domain.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.User;
import ru.alexandr.BookingCinemaTickets.domain.UserInfo;
import ru.alexandr.BookingCinemaTickets.dto.RoleDto;
import ru.alexandr.BookingCinemaTickets.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.exception.UserNotFoundException;
import ru.alexandr.BookingCinemaTickets.mapper.UserProfileInfoMapper;
import ru.alexandr.BookingCinemaTickets.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserProfileInfoMapper userProfileInfoMapper;

    private User user;
    private UserInfo userInfo;
    private Role role;
    private RoleUser roleUser;
    private UserProfileInfoDto userProfileInfoDto;

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

        userProfileInfoDto = new UserProfileInfoDto(
                1L,
                user.getUsername(),
                Set.of(new RoleDto(1L, role.getName())),
                userInfo.getEmail(),
                userInfo.getPhoneNumber(),
                userInfo.getCreatedAt().toString()
        );
    }

    @Test
    void getUserProfileInfo_ShouldReturnUserProfileInfo_WhenGetCorrectUserId() {
        when(userRepository.findByIdWithInfoAndRoles(user.getId())).thenReturn(Optional.of(user));
        when(userProfileInfoMapper.toDto(user, userInfo, Set.of(role))).thenReturn(userProfileInfoDto);

        UserProfileInfoDto result = userService.getUserProfileInfo(user.getId());

        verify(userRepository).findByIdWithInfoAndRoles(user.getId());
        verify(userProfileInfoMapper).toDto(user, userInfo, Set.of(role));

        assertThat(result).isEqualTo(userProfileInfoDto);
    }

    @Test
    void getUserProfileInfo_ShouldReturnUserNotFoundException_WhenGetNotExistUserId() {
        when(userRepository.findByIdWithInfoAndRoles(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserProfileInfo(user.getId()))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByIdWithInfoAndRoles(user.getId());
        verify(userProfileInfoMapper, never()).toDto(any(), any(), any());
    }

    @Test
    void getUserProfileInfoPage_ShouldReturnUserProfileInfoPage() {
        Page<User> userPage = new PageImpl<>(List.of(user));
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.findAllWithInfoAndRoles(pageable))
                .thenReturn(userPage);
        when(userProfileInfoMapper.toDto(user, userInfo, Set.of(role)))
                .thenReturn(userProfileInfoDto);

        Page<UserProfileInfoDto> result = userService.getUserProfileInfoPage(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(userProfileInfoDto);

        verify(userRepository).findAllWithInfoAndRoles(pageable);
        verify(userProfileInfoMapper).toDto(user, userInfo, Set.of(role));
    }
}
