package ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.parser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.alexandr.BookingCinemaTickets.application.exception.InvalidJwtTokenTypeException;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.BaseJwtTest;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.JwtTokenFactory;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.dto.AccessTokenInput;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.dto.AccessTokenPayload;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.enums.JwtTokenType;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccessTokenParserTest extends BaseJwtTest {
    private AccessTokenParser accessTokenParser;

    private AccessTokenInput accessTokenInput;

    private String token;
    private String notAccessToken;

    @BeforeAll
    void setUp() {
        accessTokenParser = new AccessTokenParser(jwtKeyProvider);

        Long userId = 1L;
        String username = "randomUsername";
        Set<String> roles = Set.of("ROLE_ADMIN", "ROLE_USER");
        accessTokenInput = new AccessTokenInput(
                userId,
                username,
                roles
        );

        JwtTokenFactory jwtTokenFactory = new JwtTokenFactory(jwtProperties, jwtKeyProvider);
        token = jwtTokenFactory.generateAccessToken(accessTokenInput);

        notAccessToken = jwtTokenFactory.generateRefreshToken(userId);
    }

    @Test
    void getUsername_ShouldReturnUsername_WhenGivenAccessToken() {
        String actual = accessTokenParser.getUsername(token);

        assertThat(actual).isEqualTo(accessTokenInput.username());
    }

    @Test
    void getUsername_ShouldThrowInvalidJwtTokenType_WhenGivenNotAccessToken() {
        assertThatThrownBy(() -> accessTokenParser.getUsername(notAccessToken))
                .isInstanceOf(InvalidJwtTokenTypeException.class);
    }

    @Test
    void getRoles_ShouldReturnRoles_WhenGivenAccessToken() {
        Set<String> actual = accessTokenParser.getRoles(token);

        assertThat(actual).isEqualTo(accessTokenInput.roles());
    }

    @Test
    void getRoles_ShouldThrowInvalidJwtTokenType_WhenGivenNotAccessToken() {
        assertThatThrownBy(() -> accessTokenParser.getRoles(notAccessToken))
                .isInstanceOf(InvalidJwtTokenTypeException.class);
    }

    @Test
    void getPayload_ShouldReturnPayload_WhenGivenAccessToken() {
        AccessTokenPayload accessTokenPayload = accessTokenParser.getPayload(token);

        assertThat(accessTokenPayload).isNotNull();
        assertThat(accessTokenPayload.getId()).isNotNull();
        assertThat(accessTokenPayload.getUserId()).isEqualTo(accessTokenInput.userId());
        assertThat(accessTokenPayload.getJwtTokenType()).isEqualTo(JwtTokenType.ACCESS);
        assertThat(accessTokenPayload.getUsername()).isEqualTo(accessTokenInput.username());
        assertThat(accessTokenPayload.getRoles()).isEqualTo(accessTokenInput.roles());
    }

    @Test
    void getPayload_ShouldThrowInvalidJwtTokenTypeException_WhenGivenAccessToken() {
        assertThatThrownBy(() -> accessTokenParser.getPayload(notAccessToken))
                .isInstanceOf(InvalidJwtTokenTypeException.class);
    }

    @Test
    void ofType_ShouldReturnTrue_WhenGivenAccessToken() {
        Boolean actual = accessTokenParser.ofType(token);

        assertThat(actual).isTrue();
    }

    @Test
    void ofType_ShouldReturnFalse_WhenGivenNotAccessToken() {
        Boolean actual = accessTokenParser.ofType(notAccessToken);

        assertThat(actual).isFalse();
    }
}