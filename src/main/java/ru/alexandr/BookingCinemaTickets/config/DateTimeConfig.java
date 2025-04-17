package ru.alexandr.BookingCinemaTickets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class DateTimeConfig {
    private final DateTimeProperties dateTimeProperties;

    public DateTimeConfig(DateTimeProperties dateTimeProperties) {
        this.dateTimeProperties = dateTimeProperties;
    }

    @Bean
    public DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern(dateTimeProperties.getFormat());
    }
}
