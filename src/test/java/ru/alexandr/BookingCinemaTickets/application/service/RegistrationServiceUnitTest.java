package ru.alexandr.BookingCinemaTickets.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.alexandr.BookingCinemaTickets.application.dto.RegisterDto;
import ru.alexandr.BookingCinemaTickets.application.dto.RoleDto;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
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
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceUnitTest {

    @InjectMocks
    private RegistrationService registrationService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleUserRepository roleUserRepository;
    @Mock
    private UserProfileInfoMapper userProfileInfoMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    private RegisterDto registerDto;
    private Role roleUser;
    private Set<Role> roles;
    private UserProfileInfoDto userProfileInfoDto;

    @BeforeEach
    void setUp() {
        roleUser = new Role("ROLE_USER");

        try {
            Field fieldId = Role.class.getDeclaredField("id");

            fieldId.setAccessible(true);

            fieldId.set(roleUser, 2L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        roles = Set.of(roleUser);

        registerDto = new RegisterDto(
                "username",
                "securityPassword",
                "email@gmail.com",
                "+79111333377"
        );


        userProfileInfoDto = new UserProfileInfoDto(
                1L,
                registerDto.username(),
                Set.of(new RoleDto(roleUser.getId(), roleUser.getName())),
                registerDto.email(),
                registerDto.phoneNumber(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        );
    }

    @Test
    void createUserWithInfo_ShouldSaveUserWithRolesAndInfo() {
        when(userRepository.existsByUsername(registerDto.username()))
                .thenReturn(false);
        when(roleRepository.findByNameIn(anyCollection()))
                .thenReturn(List.of(roleUser));
        when(userProfileInfoMapper.toDto(any(), any(), any()))
                .thenReturn(userProfileInfoDto);
        when(passwordEncoder.encode(registerDto.password()))
                .thenReturn("hashPassword");

        UserProfileInfoDto userProfileActual = registrationService.register(registerDto);

        assertThat(userProfileActual).isEqualTo(userProfileInfoDto);

        verify(userRepository)
                .existsByUsername(registerDto.username());
        verify(userRepository, times(1))
                .save(any(User.class));
        verify(roleUserRepository, times(1))
                .saveAll(anyIterable());
        verify(userProfileInfoMapper, times(1))
                .toDto(any(User.class), any(UserInfo.class), anyCollection());
    }

    @Test
    void register_ShouldThrowException_WhenUsernameAlreadyTaken() {
        when(userRepository.existsByUsername(registerDto.username())).thenReturn(true);

        assertThatThrownBy(() -> registrationService.register(registerDto))
                .isInstanceOf(UsernameAlreadyTakenException.class);
    }
}