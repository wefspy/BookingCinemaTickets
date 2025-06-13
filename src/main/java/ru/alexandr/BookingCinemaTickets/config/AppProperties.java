package ru.alexandr.BookingCinemaTickets.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private String version;

    @PostConstruct
    public void init() {
        System.out.println(
                "Приложение: " + getName() + System.lineSeparator() +
                        "Версия: " + getVersion()
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AppProperties that = (AppProperties) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getVersion(), that.getVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getVersion());
    }

    @Override
    public String toString() {
        return "AppProperties{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
