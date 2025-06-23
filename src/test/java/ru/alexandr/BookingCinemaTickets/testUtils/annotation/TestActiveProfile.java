package ru.alexandr.BookingCinemaTickets.testUtils.annotation;

import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@ActiveProfiles("test")
public @interface TestActiveProfile {
}
