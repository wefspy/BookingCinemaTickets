package ru.alexandr.BookingCinemaTickets.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alexandr.BookingCinemaTickets.application.dto.RoleDto;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.application.dto.UserRegisterDto;
import ru.alexandr.BookingCinemaTickets.application.exception.RoleNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.exception.UsernameAlreadyTakenException;
import ru.alexandr.BookingCinemaTickets.application.mapper.UserProfileInfoMapper;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.domain.model.UserInfo;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.RoleRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.RoleUserRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserRepository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceUnitTest {

    @InjectMocks
    private AuthService authService;

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
                1L,
                userRegisterDto.username(),
                Set.of(new RoleDto(roleAdmin.getId(), roleAdmin.getName()),
                        new RoleDto(roleUser.getId(), roleUser.getName())),
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

        UserProfileInfoDto userProfileActual = authService.createUserWithInfo(userRegisterDto);

        assertThat(userProfileActual).isEqualTo(userProfileInfoDto);

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

        assertThatThrownBy(() -> authService.createUserWithInfo(userRegisterDto))
                .isInstanceOf(UsernameAlreadyTakenException.class);
    }

    @Test
    void createUserWithInfo_ShouldThrowException_WhenSomeRolesNotFound() {
        when(roleRepository.findAllById(userRegisterDto.roleIds()))
                .thenReturn(Set.of(roleAdmin));

        assertThatThrownBy(() -> authService.createUserWithInfo(userRegisterDto))
                .isInstanceOf(RoleNotFoundException.class);

        verify(userRepository).save(any(User.class));
        verify(roleUserRepository, never()).saveAll(any());
        verify(userProfileInfoMapper, never()).toDto(any(), any(), any());
    }
}