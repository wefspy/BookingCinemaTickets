package ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.parser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.alexandr.BookingCinemaTickets.application.exception.ClaimNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.exception.InvalidJwtTokenTypeException;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.BaseJwtTest;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.dto.JwtTokenPayload;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.enums.JwtClaims;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.enums.JwtTokenType;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AbstractJwtTokenParserTest extends BaseJwtTest {
    private AbstractJwtTokenParser abstractJwtTokenParser;

    private UUID tokenId;
    private Long userId;
    private JwtTokenType jwtTokenType;
    private Claims claims;

    private JwtTokenType wrongJwtTokenType;
    private Claims emptyClaims;

    private String token;

    @BeforeAll
    void setUp() {
        abstractJwtTokenParser = new RefreshTokenParser(jwtKeyProvider);

        tokenId = UUID.randomUUID();
        userId = 1L;
        jwtTokenType = JwtTokenType.ACCESS;
        wrongJwtTokenType = Arrays.stream(JwtTokenType.values())
                .filter(t -> !t.equals(jwtTokenType))
                .findFirst()
                .get();


        claims = Jwts.claims()
                .id(tokenId.toString())
                .subject(userId.toString())
                .add(JwtClaims.TOKEN_TYPE.getKey(), jwtTokenType.toString())
                .build();
        emptyClaims = Jwts.claims().build();

        token = Jwts.builder()
                .claims(claims)
                .signWith(jwtKeyProvider.getSignKey())
                .compact();
    }

    @Test
    void getPayload_ShouldReturnPayload_WhenGivenAccessToken() {
        JwtTokenPayload accessTokenPayload = abstractJwtTokenParser.getPayload(token);

        assertThat(accessTokenPayload).isNotNull();
        assertThat(accessTokenPayload.getId()).isNotNull();
        assertThat(accessTokenPayload.getUserId()).isEqualTo(userId);
        assertThat(accessTokenPayload.getJwtTokenType()).isNotNull();
    }

    @Test
    void getTokenType_ShouldReturnTokenType() {
        JwtTokenType actual = abstractJwtTokenParser.getTokenType(token);

        assertThat(actual).isEqualTo(jwtTokenType);
    }

    @Test
    void getTokenId_ShouldReturnTokenId() {
        UUID actual = abstractJwtTokenParser.getTokenId(token);

        assertThat(actual).isEqualTo(tokenId);
    }

    @Test
    void getUserId_ShouldReturnUserId() {
        Long actual = abstractJwtTokenParser.getUserId(token);

        assertThat(actual).isEqualTo(userId);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenGivenValidToken() {
        Boolean actual = abstractJwtTokenParser.isValid(token);

        assertThat(actual).isTrue();
    }

    @Test
    void isValid_ShouldReturnFalse_WhenGivenInvalidToken() {
        Boolean actual = abstractJwtTokenParser.isValid("InvalidToken");

        assertThat(actual).isFalse();
    }

    @Test
    void isValid_ShouldReturnFalse_WhenGivenExpiredToken() {
        String expiredToken = Jwts.builder()
                .claims(claims)
                .issuedAt(Date.from(Instant.now().minusSeconds(3600)))
                .expiration(Date.from(Instant.now().minusSeconds(1800)))
                .signWith(jwtKeyProvider.getSignKey())
                .compact();

        Boolean actual = abstractJwtTokenParser.isValid(expiredToken);

        assertThat(actual).isFalse();
    }

    @Test
    void isValid_ShouldReturnFalse_WhenGivenSomeoneElseToken() {
        byte[] keyBytes = Decoders.BASE64URL.decode(jwtProperties.getSecret() + "jWj");
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

        String someoneElseToken = Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();

        Boolean actual = abstractJwtTokenParser.isValid(someoneElseToken);

        assertThat(actual).isFalse();
    }

    @Test
    void getClaims_ShouldReturnCorrectClaims() {
        Claims actual = abstractJwtTokenParser.getClaims(token);

        assertThat(actual).isEqualTo(claims);
    }

    @Test
    void assertType_ShouldVoid_WhenGivenCorrectTokenType() {
        abstractJwtTokenParser.assertType(claims, jwtTokenType);
    }

    @Test
    void assertType_ShouldThrowInvalidJwtTokenType_WhenGivenWrongTokenType() {
        assertThatThrownBy(() -> abstractJwtTokenParser.assertType(claims, wrongJwtTokenType))
                .isInstanceOf(InvalidJwtTokenTypeException.class);
    }

    @Test
    void ofType_ShouldReturnTrue_WhenGivenRightTokenType() {
        Boolean actual = abstractJwtTokenParser.ofType(token, jwtTokenType);

        assertThat(actual).isTrue();
    }

    @Test
    void ofType_ShouldReturnFalse_WhenGivenWrongTokenType() {
        Boolean actual = abstractJwtTokenParser.ofType(token, wrongJwtTokenType);

        assertThat(actual).isFalse();
    }

    @Test
    void getTokenType_ShouldReturnTokenType_WhenGivenRightClaims() {
        JwtTokenType actual = abstractJwtTokenParser.getTokenType(claims);

        assertThat(actual).isEqualTo(jwtTokenType);
    }

    @Test
    void getTokenType_ShouldThrowClaimNotFound_WhenGivenWrongClaims() {
        assertThatThrownBy(() -> abstractJwtTokenParser.getTokenType(emptyClaims))
                .isInstanceOf(ClaimNotFoundException.class);
    }

    @Test
    void getTokenId_ShouldReturnTokenId_WhenGivenRightClaims() {
        UUID actual = abstractJwtTokenParser.getTokenId(claims);

        assertThat(actual).isEqualTo(tokenId);
    }

    @Test
    void getTokenId_ShouldThrowClaimNotFound_WhenGivenWrongClaims() {
        assertThatThrownBy(() -> abstractJwtTokenParser.getTokenId(emptyClaims))
                .isInstanceOf(ClaimNotFoundException.class);
    }

    @Test
    void getUserId_ShouldReturnTokenId_WhenGivenRightClaims() {
        Long actual = abstractJwtTokenParser.getUserId(claims);

        assertThat(actual).isEqualTo(userId);
    }

    @Test
    void getUserId_ShouldThrowClaimNotFound_WhenGivenWrongClaims() {
        assertThatThrownBy(() -> abstractJwtTokenParser.getUserId(emptyClaims))
                .isInstanceOf(ClaimNotFoundException.class);
    }

    @Test
    void getClaim_ShouldReturnSpecifiedClaim_WhenGivenRightClaims() {
        String claimKey = "customClaim";
        String claimValue = "claimValue";
        Claims customClaims = Jwts.claims()
                .add(claimKey, claimValue)
                .build();

        Object actual = abstractJwtTokenParser.getClaim(claimKey, customClaims);

        assertThat(actual).isEqualTo(claimValue);
    }

    @Test
    void getClaim_ShouldThrowClaimNotFound_WhenGivenWrongClaims() {
        assertThatThrownBy(() -> abstractJwtTokenParser.getClaim("claimKey", emptyClaims))
                .isInstanceOf(ClaimNotFoundException.class);
    }
}