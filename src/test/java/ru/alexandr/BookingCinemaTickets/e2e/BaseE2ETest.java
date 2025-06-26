package ru.alexandr.BookingCinemaTickets.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.PostgreSQLTestContainer;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.TestActiveProfile;
import ru.alexandr.BookingCinemaTickets.testUtils.configuration.property.TestWebServerProperties;
import ru.alexandr.BookingCinemaTickets.testUtils.db.DatabaseCleaner;

import java.time.Duration;

@TestActiveProfile
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PostgreSQLTestContainer
public abstract class BaseE2ETest {
    @Autowired
    private TestWebServerProperties testWebServerProperties;

    private WebDriver driver;
    private WebDriverWait wait;
    @LocalServerPort
    private Integer port;
    private String baseUrl;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DatabaseCleaner dbCleaner;

    @BeforeAll
    static void beforeAllBase() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUpBase() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        baseUrl = "http://" + getHost() + ":" + getPort();
    }

    @AfterEach
    void tearDownBase() {
        if (driver != null) {
            driver.quit();
        }

        dbCleaner.resetDatabase();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebDriverWait getWait() {
        return wait;
    }

    public Integer getPort() {
        return port;
    }

    public String getHost() {
        return testWebServerProperties.getHost();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    protected void navigateToPage(String path) {
        driver.get(baseUrl + path);
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
