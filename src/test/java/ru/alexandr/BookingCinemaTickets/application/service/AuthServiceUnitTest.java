package ru.alexandr.BookingCinemaTickets.application.service;

import io.jsonwebtoken.ExpiredJwtException;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import ru.alexandr.BookingCinemaTickets.application.dto.LoginRequestDto;
import ru.alexandr.BookingCinemaTickets.application.dto.LoginResponseDto;
import ru.alexandr.BookingCinemaTickets.application.exception.UserNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.mapper.AccessTokenInputMapper;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.UserDetailsImpl;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.JwtTokenFactory;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.dto.AccessTokenInput;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.parser.RefreshTokenParser;
import ru.alexandr.BookingCinemaTickets.testUtils.asserts.LoginResponseDtoAssert;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceUnitTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetailsImpl userDetails;
    @Mock
    private AccessTokenInputMapper accessTokenInputMapper;
    @Mock
    private AccessTokenInput accessTokenInput;
    @Mock
    private JwtTokenFactory jwtTokenFactory;
    @Mock
    private RefreshTokenParser refreshTokenParser;
    @Mock
    private UserRepository userRepository;
    @Mock
    private User user;

    private final String username = "username";
    private final String password = "password";
    private final Long userId = 1L;
    private final LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);
    private final String accessToken = "accessToken";
    private final String refreshToken = "refreshToken";
    private final LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken, refreshToken);

    @Test
    void login_ShouldReturnLoginResponseDto_WhenLoginSuccess() {
        when(authenticationManager.authenticate(argThat(auth ->
                auth instanceof UsernamePasswordAuthenticationToken
                && auth.getPrincipal().equals(loginRequestDto.username())
                && auth.getCredentials().equals(loginRequestDto.password())
        ))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(accessTokenInputMapper.from(userDetails)).thenReturn(accessTokenInput);
        when(accessTokenInput.userId()).thenReturn(userId);
        when(jwtTokenFactory.generateAccessToken(accessTokenInput)).thenReturn(loginResponseDto.accessToken());
        when(jwtTokenFactory.generateRefreshToken(userId)).thenReturn(loginResponseDto.refreshToken());

        LoginResponseDto actualDto = authService.login(loginRequestDto);

        LoginResponseDtoAssert.assertThat(actualDto).isEqualTo(loginResponseDto);

        verify(authenticationManager, times(1)).authenticate(any());
        verifyNoMoreInteractions(authenticationManager);
    }

    @Test
    void login_ShouldThrowAuthenticationException_WhenLoginFailed() {
        when(authenticationManager.authenticate(argThat(auth ->
                auth instanceof UsernamePasswordAuthenticationToken
                        && auth.getPrincipal().equals(loginRequestDto.username())
                        && auth.getCredentials().equals(loginRequestDto.password())
        ))).thenThrow(new BadCredentialsException("Invalid credentials"));

        Assertions.assertThatThrownBy(() -> authService.login(loginRequestDto))
                .isInstanceOf(AuthenticationException.class);

        verify(authenticationManager, times(1)).authenticate(any());
        verifyNoMoreInteractions(authenticationManager);
    }

    @Test
    void refresh_ShouldReturnRefreshResponseDto_WhenRefreshSuccess() {
        when(refreshTokenParser.getUserId(refreshToken)).thenReturn(userId);
        when(userRepository.findByIdWithRoles(userId)).thenReturn(Optional.of(user));
        when(accessTokenInputMapper.from(user)).thenReturn(accessTokenInput);
        when(accessTokenInput.userId()).thenReturn(userId);
        when(jwtTokenFactory.generateAccessToken(accessTokenInput)).thenReturn(accessToken);
        when(jwtTokenFactory.generateRefreshToken(userId)).thenReturn(refreshToken);

        LoginResponseDto actualDto = authService.refresh(refreshToken);

        LoginResponseDtoAssert.assertThat(actualDto)
                .isNotNull()
                .hasAccessToken(accessToken)
                .hasRefreshToken(refreshToken);

        verify(refreshTokenParser, times(1)).getUserId(any());
        verify(userRepository, times(1)).findByIdWithRoles(userId);
    }

    @Test
    void refresh_ShouldThrowException_WhenGivenInvalidToken() {
        when(refreshTokenParser.getUserId(refreshToken)).thenThrow(new IllegalArgumentException("Invalid token"));

        Assertions.assertThatThrownBy(() -> authService.refresh(refreshToken)).isInstanceOf(Exception.class);

        verify(refreshTokenParser, times(1)).getUserId(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void refresh_ShouldThrowException_WhenNotFoundUser() {
        when(refreshTokenParser.getUserId(refreshToken)).thenReturn(userId);
        when(userRepository.findByIdWithRoles(userId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> authService.refresh(refreshToken)).isInstanceOf(UserNotFoundException.class);

        verify(refreshTokenParser, times(1)).getUserId(any());
        verifyNoMoreInteractions(userRepository);
    }
}