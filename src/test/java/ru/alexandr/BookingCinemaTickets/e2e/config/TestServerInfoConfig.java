package ru.alexandr.BookingCinemaTickets.e2e.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.alexandr.BookingCinemaTickets.e2e.config.property.TestServerInfoProperties;


@TestConfiguration
@EnableConfigurationProperties(TestServerInfoProperties.class)
public class TestServerInfoConfig {

    @Bean
    public TestServerInfoProperties testServerInfoProperties() {
        return new TestServerInfoProperties();
    }
}
