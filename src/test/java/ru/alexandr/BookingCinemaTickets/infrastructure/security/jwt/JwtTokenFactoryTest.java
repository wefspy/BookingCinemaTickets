package ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.dto.AccessTokenInput;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenFactoryTest extends BaseJwtTest {
    private static JwtTokenFactory jwtTokenFactory;

    private AccessTokenInput accessTokenInput;

    @BeforeAll
    void setUp() {
        jwtTokenFactory = new JwtTokenFactory(jwtProperties, jwtKeyProvider);

        accessTokenInput = new AccessTokenInput(
                1L,
                "username",
                Set.of("ROLE_USER", "ROLE_ADMIN")
        );
    }

    @Test
    void generateAccessToken_ShouldReturnNotBlankToken() {
        String token = jwtTokenFactory.generateAccessToken(accessTokenInput);

        assertThat(token).isNotBlank();
    }

    @Test
    void generateAccessToken_ShouldReturnDifferentTokens_WhenGivenSameInput() {
        String token1 = jwtTokenFactory.generateAccessToken(accessTokenInput);
        String token2 = jwtTokenFactory.generateAccessToken(accessTokenInput);

        assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    void generateRefreshToken_ShouldReturnNotBlankToken() {
        String token = jwtTokenFactory.generateRefreshToken(accessTokenInput.userId());

        assertThat(token).isNotBlank();
    }

    @Test
    void generateRefreshToken_ShouldReturnDifferentTokens_WhenGivenSameInput() {
        String token1 = jwtTokenFactory.generateRefreshToken(accessTokenInput.userId());
        String token2 = jwtTokenFactory.generateRefreshToken(accessTokenInput.userId());

        assertThat(token1).isNotEqualTo(token2);
    }
}