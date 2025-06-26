package ru.alexandr.BookingCinemaTickets.testUtils.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import ru.alexandr.BookingCinemaTickets.testUtils.configuration.property.TestWebServerProperties;

@TestConfiguration
@EnableConfigurationProperties({TestWebServerProperties.class})
public class TestWebServerConfiguration {
}
