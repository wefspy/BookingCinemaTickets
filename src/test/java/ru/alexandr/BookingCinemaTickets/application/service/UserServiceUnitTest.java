package ru.alexandr.BookingCinemaTickets.application.service;

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
import ru.alexandr.BookingCinemaTickets.application.dto.RoleDto;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.application.exception.UserNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.mapper.UserProfileInfoMapper;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.domain.model.UserInfo;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserRepository;
import ru.alexandr.BookingCinemaTickets.testUtils.factory.TestEntityFactory;

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
    private UserProfileInfoDto userProfileInfoDto;

    @BeforeEach
    void setUp() {
        user = TestEntityFactory.user(1L, "username", "password");
        userInfo = TestEntityFactory.userInfo(user.getId(), user, LocalDateTime.now());
        role = new Role("USER");

        userProfileInfoDto = new UserProfileInfoDto(
                user.getId(),
                user.getUsername(),
                Set.of(new RoleDto(role.getId(), role.getName())),
                userInfo.getEmail(),
                userInfo.getPhoneNumber(),
                userInfo.getCreatedAt()
        );
    }

    @Test
    void getUserProfileInfo_ShouldReturnUserProfileInfo_WhenGetCorrectUserId() {
        when(userRepository.findByIdWithInfoAndRoles(user.getId())).thenReturn(Optional.of(user));
        when(userProfileInfoMapper.toDto(user)).thenReturn(userProfileInfoDto);

        UserProfileInfoDto actualDto = userService.getUserProfileInfo(user.getId());

        verify(userRepository, times(1)).findByIdWithInfoAndRoles(user.getId());
        verify(userProfileInfoMapper, times(1)).toDto(user);

        assertThat(actualDto).isEqualTo(userProfileInfoDto);
    }

    @Test
    void getUserProfileInfo_ShouldThrowUserNotFoundException_WhenGetNotExistUserId() {
        when(userRepository.findByIdWithInfoAndRoles(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserProfileInfo(user.getId()))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository, times(1)).findByIdWithInfoAndRoles(user.getId());
        verify(userProfileInfoMapper, never()).toDto(any());
    }

    @Test
    void getUserProfileInfoPage_ShouldReturnUserProfileInfoPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user));

        when(userRepository.findAllWithInfoAndRoles(pageable)).thenReturn(userPage);
        when(userProfileInfoMapper.toDto(user)).thenReturn(userProfileInfoDto);

        Page<UserProfileInfoDto> actualPage = userService.getUserProfileInfoPage(pageable);

        assertThat(actualPage.getContent())
                .hasSize(1)
                .contains(userProfileInfoDto);

        verify(userRepository, times(1)).findAllWithInfoAndRoles(pageable);
        verify(userProfileInfoMapper, times(1)).toDto(user);
    }
}
