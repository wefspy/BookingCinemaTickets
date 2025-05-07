package ru.alexandr.BookingCinemaTickets.e2e.config.property;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

@ConfigurationProperties(prefix = "server")
public class TestServerInfoProperties {
    private String host = "localhost";
    private Integer port = 8080;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getBaseUrl() {
        return String.format("http://%s:%d", getHost(), getPort());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())  {
            return false;
        }
        TestServerInfoProperties that = (TestServerInfoProperties) o;

        return Objects.equals(getHost(), that.getHost())
                && Objects.equals(getPort(), that.getPort());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHost(),
                getPort());
    }

    @Override
    public String toString() {
        return "TestServerInfoProperties{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
