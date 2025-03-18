package ru.alexandr.BookingCinemaTickets;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @PostConstruct
    public void init() {
        System.out.println(
                "Приложение: " + appName + System.lineSeparator() +
                "Версия: " + appVersion
        );
    }
}
