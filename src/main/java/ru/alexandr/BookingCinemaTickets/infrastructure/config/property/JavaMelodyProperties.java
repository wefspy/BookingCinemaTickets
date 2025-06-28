package ru.alexandr.BookingCinemaTickets.infrastructure.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

@ConfigurationProperties(prefix = "javamelody.init-parameters")
public class JavaMelodyProperties {
    private String monitoringPath;

    public String getMonitoringPath() {
        return monitoringPath;
    }

    public void setMonitoringPath(String monitoringPath) {
        this.monitoringPath = monitoringPath;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JavaMelodyProperties that = (JavaMelodyProperties) o;

        return Objects.equals(getMonitoringPath(), that.getMonitoringPath());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMonitoringPath());
    }

    @Override
    public String toString() {
        return "JavaMelodyProperties{" +
                "path='" + monitoringPath + '\'' +
                '}';
    }
}
