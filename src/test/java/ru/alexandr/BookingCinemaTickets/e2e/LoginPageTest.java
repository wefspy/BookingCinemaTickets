package ru.alexandr.BookingCinemaTickets.e2e;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.alexandr.BookingCinemaTickets.application.dto.RegisterDto;
import ru.alexandr.BookingCinemaTickets.application.service.RegistrationService;
import ru.alexandr.BookingCinemaTickets.e2e.config.property.TestServerInfoProperties;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserRepository;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LoginPageTest {
    private String loginPageUrl;

    @Autowired
    private TestServerInfoProperties testServerInfoProperties;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private UserRepository userRepository;

    private WebDriver driver;

    private RegisterDto registerDto;

    @BeforeAll
    void setup() {
        loginPageUrl = testServerInfoProperties.getBaseUrl() + "/login";

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        registerDto = new RegisterDto(
                "uniqueUsername",
                "strongPassword123",
                "myEmail@gmail.com",
                null
        );
        registrationService.register(registerDto);
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }

        userRepository.deleteAll();
    }

    @Test
    void shouldLoginSuccessfullyWithCorrectCredentials() {
        driver.get(loginPageUrl);

        driver.findElement(By.name("username")).sendKeys(registerDto.username());
        driver.findElement(By.name("password")).sendKeys(registerDto.password());
        driver.findElement(By.tagName("button")).click();

        assertThat(driver.getCurrentUrl()).contains("/users");
    }

    @Test
    void shouldLogoutSuccessfully() {
        driver.get(loginPageUrl);
        driver.findElement(By.name("username")).sendKeys(registerDto.username());
        driver.findElement(By.name("password")).sendKeys(registerDto.password());
        driver.findElement(By.tagName("button")).click();

        WebElement logoutButton = driver.findElement(By.id("logout-button"));
        logoutButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains(loginPageUrl));

        assertThat(driver.getCurrentUrl()).contains(loginPageUrl);
    }
}
