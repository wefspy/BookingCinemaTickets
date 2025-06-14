package ru.alexandr.BookingCinemaTickets.infrastructure.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

@ConfigurationProperties(prefix = "app.datetime")
public class DateTimeProperties {
    private String format;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DateTimeProperties that = (DateTimeProperties) o;

        return Objects.equals(getFormat(), that.getFormat());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getFormat());
    }

    @Override
    public String toString() {
        return "DateTimeProperties{" +
                "format='" + format + '\'' +
                '}';
    }
}
