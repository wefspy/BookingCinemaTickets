package ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import ru.alexandr.BookingCinemaTickets.infrastructure.config.property.JwtProperties;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseJwtTest {
    protected JwtProperties jwtProperties;
    protected JwtKeyProvider jwtKeyProvider;

    private final String testSecret = "YWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYQ";

    @BeforeAll
    void baseSetUp() {
        jwtProperties = new JwtProperties();
        jwtProperties.setSecret(testSecret);
        jwtProperties.setAccessExpiration(Duration.ofHours(1));
        jwtProperties.setRefreshExpiration(Duration.ofDays(30));

        jwtKeyProvider = new JwtKeyProvider(jwtProperties);
    }
}
