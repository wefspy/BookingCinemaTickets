package ru.alexandr.BookingCinemaTickets.infrastructure.configuration;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.alexandr.BookingCinemaTickets.domain.Movie;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class ContainersConfiguration {

    @Bean
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public Map<Long, Movie> movieContainer() {
        return new ConcurrentHashMap<Long, Movie>();
    }
}
