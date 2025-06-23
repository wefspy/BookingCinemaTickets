package ru.alexandr.BookingCinemaTickets.testUtils.annotation;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import ru.alexandr.BookingCinemaTickets.testUtils.configuration.PostgresTestContainerConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PostgresTestContainerConfiguration.class)
public @interface PostgreSQLTestContainer {
}
