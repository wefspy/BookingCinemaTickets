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
import ru.alexandr.BookingCinemaTickets.domain.model.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.domain.model.UserInfo;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.RoleRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.RoleUserRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserInfoRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.RoleEnum;
import ru.alexandr.BookingCinemaTickets.testUtils.factory.TestEntityFactory;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceUnitTest {

    @InjectMocks
    private RegistrationService registrationService;

    @Mock
    private UserInfoRepository userInfoRepository;
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

    private final RegisterDto registerDto = new RegisterDto(
            "username",
            "securityPassword",
            "email@gmail.com",
            "+79111333377"
    );
    private final String hashedPassword = "hashPassword";
    private User user;
    private UserInfo userInfo;
    private Role role;
    private List<Role> roles;
    private RoleUser roleUser;
    private UserProfileInfoDto userProfileInfoDto;

    @BeforeEach
    void setUp() {
        user = TestEntityFactory.user(1L, registerDto.username(), registerDto.password());
        userInfo = TestEntityFactory.userInfo(user.getId(), user, LocalDateTime.now());
        role = TestEntityFactory.role(1L, RoleEnum.USER.name());
        roles = List.of(role);
        roleUser = TestEntityFactory.roleUser(1L, user, role);

        userProfileInfoDto = new UserProfileInfoDto(
                user.getId(),
                user.getUsername(),
                List.of(new RoleDto(role.getId(), role.getName())),
                userInfo.getEmail(),
                userInfo.getPhoneNumber(),
                userInfo.getCreatedAt()
        );
    }

    @Test
    void register_ShouldSaveUserWithRolesAndInfo() {
        when(userRepository.existsByUsername(registerDto.username())).thenReturn(false);
        when(passwordEncoder.encode(registerDto.password())).thenReturn(hashedPassword);
        when(userRepository.save(argThat(u ->
                u.getUsername().equals(registerDto.username())
                && u.getPasswordHash().equals(hashedPassword)
        ))).thenReturn(user);
        when(userInfoRepository.save(argThat(uf ->
                uf.getUser().equals(user)
                && uf.getEmail().equals(registerDto.email())
                && uf.getPhoneNumber().equals(registerDto.phoneNumber())
        ))).thenReturn(userInfo);
        when(roleRepository.findByNameIn(anyCollection())).thenReturn(roles);
        when(userProfileInfoMapper.toDto(user, userInfo, roles))
                .thenReturn(userProfileInfoDto);

        UserProfileInfoDto userProfileActual = registrationService.register(registerDto);

        assertThat(userProfileActual).isEqualTo(userProfileInfoDto);

        verify(userRepository).existsByUsername(registerDto.username());
        verify(passwordEncoder).encode(registerDto.password());
        verify(userRepository, times(1)).save(argThat(u ->
                u.getUsername().equals(registerDto.username())
                && u.getPasswordHash().equals(hashedPassword)));
        verify(userInfoRepository, times(1)).save(argThat(uf ->
                uf.getUser().equals(user)
                && uf.getEmail().equals(registerDto.email())
                && uf.getPhoneNumber().equals(registerDto.phoneNumber())));
        verify(roleRepository, times(1)).findByNameIn(anyCollection());
        verify(roleUserRepository, times(1)).saveAll(anyIterable());
        verify(userProfileInfoMapper, times(1)).toDto(user, userInfo, roles);
    }

    @Test
    void register_ShouldThrowException_WhenUsernameAlreadyTaken() {
        when(userRepository.existsByUsername(registerDto.username())).thenReturn(true);

        assertThatThrownBy(() -> registrationService.register(registerDto))
                .isInstanceOf(UsernameAlreadyTakenException.class);

        verify(userRepository, never()).save(any());
        verify(userInfoRepository, never()).save(any());
        verify(roleUserRepository, never()).saveAll(anyIterable());
    }
}