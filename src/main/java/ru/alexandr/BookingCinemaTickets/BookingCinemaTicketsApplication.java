package ru.alexandr.BookingCinemaTickets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BookingCinemaTicketsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingCinemaTicketsApplication.class, args);
    }

}
