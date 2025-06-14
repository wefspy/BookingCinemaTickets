package ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.parser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.BaseJwtTest;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.JwtTokenFactory;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.dto.AccessTokenInput;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenParserTest extends BaseJwtTest {
    private RefreshTokenParser refreshTokenParser;

    private AccessTokenInput accessTokenInput;

    private String token;
    private String notRefreshToken;

    @BeforeAll
    void setUp() {
        refreshTokenParser = new RefreshTokenParser(jwtKeyProvider);

        Long userId = 1L;
        String username = "randomUsername";
        Set<String> roles = Set.of("ROLE_ADMIN", "ROLE_USER");
        accessTokenInput = new AccessTokenInput(
                userId,
                username,
                roles
        );

        JwtTokenFactory jwtTokenFactory = new JwtTokenFactory(jwtProperties, jwtKeyProvider);
        token = jwtTokenFactory.generateRefreshToken(userId);

        notRefreshToken = jwtTokenFactory.generateAccessToken(accessTokenInput);
    }
    @Test
    void ofType_ShouldReturnTrue_WhenGivenAccessToken() {
        Boolean actual = refreshTokenParser.ofType(token);

        assertThat(actual).isTrue();
    }

    @Test
    void ofType_ShouldReturnFalse_WhenGivenNotAccessToken() {
        Boolean actual = refreshTokenParser.ofType(notRefreshToken);

        assertThat(actual).isFalse();
    }
}