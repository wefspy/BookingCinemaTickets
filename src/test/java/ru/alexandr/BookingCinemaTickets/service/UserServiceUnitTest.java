package ru.alexandr.BookingCinemaTickets.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alexandr.BookingCinemaTickets.domain.Role;
import ru.alexandr.BookingCinemaTickets.domain.User;
import ru.alexandr.BookingCinemaTickets.domain.UserInfo;
import ru.alexandr.BookingCinemaTickets.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.dto.UserRegisterDto;
import ru.alexandr.BookingCinemaTickets.exception.RoleNotFoundException;
import ru.alexandr.BookingCinemaTickets.exception.UsernameAlreadyTakenException;
import ru.alexandr.BookingCinemaTickets.mapper.UserProfileInfoMapper;
import ru.alexandr.BookingCinemaTickets.repository.RoleRepository;
import ru.alexandr.BookingCinemaTickets.repository.RoleUserRepository;
import ru.alexandr.BookingCinemaTickets.repository.UserRepository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleUserRepository roleUserRepository;
    @Mock
    private UserProfileInfoMapper userProfileInfoMapper;

    private UserRegisterDto userRegisterDto;
    private Role roleAdmin;
    private Role roleUser;
    private Set<Role> roles;
    private UserProfileInfoDto userProfileInfoDto;

    @BeforeEach
    void setUp() {
        roleAdmin = new Role("ADMIN");
        roleUser = new Role("USER");

        try {
            Field fieldId = Role.class.getDeclaredField("id");

            fieldId.setAccessible(true);

            fieldId.set(roleAdmin, 1L);
            fieldId.set(roleUser, 2L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        roles = Set.of(roleAdmin, roleUser);

        userRegisterDto = new UserRegisterDto(
                "username",
                "securityPassword",
                Set.of(roleAdmin.getId(), roleUser.getId()),
                "email@gmail.com",
                "+79111333377",
                LocalDateTime.now()
        );


        userProfileInfoDto = new UserProfileInfoDto(
                userRegisterDto.username(),
                Set.of(roleAdmin.getName(), roleUser.getName()),
                userRegisterDto.email(),
                userRegisterDto.phoneNumber(),
                userRegisterDto.createdAt().format(DateTimeFormatter.ISO_DATE_TIME)
        );
    }

    @Test
    void createUserWithInfo_ShouldSaveUserWithRolesAndUserInfo() {
        when(userRepository.existsByUsername(userRegisterDto.username())).thenReturn(false);
        when(roleRepository.findAllById(userRegisterDto.roleIds()))
                .thenReturn(roles);
        when(userProfileInfoMapper.toDto(any(User.class), any(UserInfo.class), anySet()))
                .thenReturn(userProfileInfoDto);

        userService.createUserWithInfo(userRegisterDto);
        
        verify(userRepository)
                .existsByUsername(userRegisterDto.username());
        verify(userRepository, times(1))
                .save(any(User.class));
        verify(roleRepository, times(1))
                .findAllById(eq(userRegisterDto.roleIds()));
        verify(roleUserRepository, times(1))
                .saveAll(anyIterable());
        verify(userProfileInfoMapper, times(1))
                .toDto(any(User.class), any(UserInfo.class), eq(roles));
    }

    @Test
    void createUserWithInfo_ShouldThrowException_WhenUsernameAlreadyTaken() {
        when(userRepository.existsByUsername(userRegisterDto.username())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUserWithInfo(userRegisterDto))
                .isInstanceOf(UsernameAlreadyTakenException.class);
    }

    @Test
    void createUserWithInfo_ShouldThrowException_WhenSomeRolesNotFound() {
        when(roleRepository.findAllById(userRegisterDto.roleIds()))
                .thenReturn(Set.of(roleAdmin));

        assertThatThrownBy(() -> userService.createUserWithInfo(userRegisterDto))
                .isInstanceOf(RoleNotFoundException.class);

        verify(userRepository).save(any(User.class));
        verify(roleUserRepository, never()).saveAll(any());
        verify(userProfileInfoMapper, never()).toDto(any(), any(), any());
    }
}