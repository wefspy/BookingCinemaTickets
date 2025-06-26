package ru.alexandr.BookingCinemaTickets.e2e;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alexandr.BookingCinemaTickets.application.dto.RegisterDto;
import ru.alexandr.BookingCinemaTickets.application.service.RegistrationService;
import ru.alexandr.BookingCinemaTickets.controller.web.enums.ToastType;
import ru.alexandr.BookingCinemaTickets.e2e.fragment.ToastFragment;
import ru.alexandr.BookingCinemaTickets.e2e.page.LoginPage;
import ru.alexandr.BookingCinemaTickets.testUtils.asserts.ToastAssert;
import ru.alexandr.BookingCinemaTickets.testUtils.constant.ControllerUrls;

public class LoginE2ETest extends BaseE2ETest {
    @Autowired
    private RegistrationService registrationService;
    private String pageFullURL;
    private final RegisterDto registerDto = new RegisterDto(
            "test-username",
            "security-password",
            "email@gmail.com",
            null
    );

    @BeforeEach
    void setUp() {
        registrationService.register(registerDto);
        pageFullURL = getBaseUrl() + ControllerUrls.LOGIN;
    }

    @Test
    void login_Successful_WhenGivenCorrectlyCredentials() {
        navigateToPage(ControllerUrls.LOGIN);
        LoginPage loginPage = new LoginPage(getDriver());
        ToastFragment toast = new ToastFragment(getDriver());

        loginPage.login(registerDto.username(), registerDto.password());

        ToastAssert.assertThat(toast).isNotVisible();
        Assertions.assertThat(getDriver().getCurrentUrl()).isNotEqualTo(pageFullURL);
    }

    @Test
    void login_Failed_WhenGivenInCorrectlyCredentials() {
        navigateToPage(ControllerUrls.LOGIN);
        LoginPage loginPage = new LoginPage(getDriver());
        ToastFragment toast = new ToastFragment(getDriver());

        loginPage.login(registerDto.username() + "NOT-EXISTS", registerDto.password());

        ToastAssert.assertThat(toast)
                .isVisible()
                .hasType(ToastType.ERROR);
        Assertions.assertThat(getDriver().getCurrentUrl()).isEqualTo(pageFullURL);    }
}
