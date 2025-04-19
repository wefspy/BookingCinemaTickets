package ru.alexandr.BookingCinemaTickets.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alexandr.BookingCinemaTickets.domain.Role;
import ru.alexandr.BookingCinemaTickets.domain.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.User;
import ru.alexandr.BookingCinemaTickets.domain.UserInfo;
import ru.alexandr.BookingCinemaTickets.dto.RoleDto;
import ru.alexandr.BookingCinemaTickets.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.exception.UserNotFoundException;
import ru.alexandr.BookingCinemaTickets.mapper.UserProfileInfoMapper;
import ru.alexandr.BookingCinemaTickets.repository.UserInfoRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserInfoServiceUnitTest {

    @InjectMocks
    private UserInfoService userInfoService;

    @Mock
    private UserInfoRepository userInfoRepository;
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
    void getUserProfileInfo_shouldReturnUserProfileInfo_whenGetCorrectUserId() {
        when(userInfoRepository.findById(user.getId())).thenReturn(Optional.of(userInfo));
        when(userProfileInfoMapper.toDto(user, userInfo, Set.of(role))).thenReturn(userProfileInfoDto);

        UserProfileInfoDto result = userInfoService.getUserProfileInfo(user.getId());

        verify(userInfoRepository).findById(user.getId());
        verify(userProfileInfoMapper).toDto(user, userInfo, Set.of(role));

        assertThat(result).isEqualTo(userProfileInfoDto);
    }

    @Test
    void getUserProfileInfo_shouldReturnUserNotFoundException_whenGetNotExistUserId() {
        when(userInfoRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userInfoService.getUserProfileInfo(user.getId()))
                .isInstanceOf(UserNotFoundException.class);

        verify(userInfoRepository).findById(user.getId());
        verify(userProfileInfoMapper, never()).toDto(any(), any(), any());
    }
}
