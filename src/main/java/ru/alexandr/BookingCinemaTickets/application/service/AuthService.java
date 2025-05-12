package ru.alexandr.BookingCinemaTickets.application.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
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

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenFactory jwtTokenFactory;
    private final RefreshTokenParser refreshTokenParser;
    private final UserRepository userRepository;
    private final AccessTokenInputMapper accessTokenInputMapper;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtTokenFactory jwtTokenFactory,
                       RefreshTokenParser refreshTokenParser,
                       UserRepository userRepository,
                       AccessTokenInputMapper accessTokenInputMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenFactory = jwtTokenFactory;
        this.refreshTokenParser = refreshTokenParser;
        this.userRepository = userRepository;
        this.accessTokenInputMapper = accessTokenInputMapper;
    }

    public LoginResponseDto login(LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        AccessTokenInput accessTokenInput = accessTokenInputMapper.from(userDetails);

        return new LoginResponseDto(
                jwtTokenFactory.generateAccessToken(accessTokenInput),
                jwtTokenFactory.generateRefreshToken(accessTokenInput.userId())
        );
    }

    public LoginResponseDto refresh(String refreshToken) {
        Long userId = refreshTokenParser.getUserId(refreshToken);
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("В токен зашит id %s пользователя, которого не существует", userId))
                );

        AccessTokenInput accessTokenInput = accessTokenInputMapper.from(user);

        return new LoginResponseDto(
                jwtTokenFactory.generateAccessToken(accessTokenInput),
                jwtTokenFactory.generateRefreshToken(accessTokenInput.userId())
        );
    }
}
