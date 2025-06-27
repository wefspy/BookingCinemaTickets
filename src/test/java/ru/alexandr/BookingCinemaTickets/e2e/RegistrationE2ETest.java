package ru.alexandr.BookingCinemaTickets.e2e;

import org.junit.jupiter.api.Test;
import ru.alexandr.BookingCinemaTickets.application.dto.RegisterDto;
import ru.alexandr.BookingCinemaTickets.controller.web.enums.ToastType;
import ru.alexandr.BookingCinemaTickets.testUtils.ui.fragment.ToastFragment;
import ru.alexandr.BookingCinemaTickets.testUtils.ui.page.LoginPage;
import ru.alexandr.BookingCinemaTickets.testUtils.ui.page.RegistrationPage;
import ru.alexandr.BookingCinemaTickets.testUtils.asserts.ToastAssert;
import ru.alexandr.BookingCinemaTickets.testUtils.constant.ControllerUrls;

public class RegistrationE2ETest extends BaseE2ETest {
    private final RegisterDto registerDto = new RegisterDto(
            "test-username",
            "security-password",
            "email@gmail.com",
            null
    );

    @Test
    void register_ShouldRedirectToLoginPageAndHaveOpportunityLogin_WhenCorrectCredentials() {
        navigateToPage(ControllerUrls.REGISTRATION);
        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        LoginPage loginPage = new LoginPage(getDriver());
        ToastFragment toast = new ToastFragment(getDriver());

        registrationPage.enterUsername(registerDto.username())
                .enterPassword(registerDto.password())
                .enterConfirmPassword(registerDto.password())
                .enterEmail(registerDto.email())
                .clickRegisterButton();

        ToastAssert.assertThat(toast).isNotVisible();

        loginPage.enterUsername(registerDto.password())
                .enterPassword(registerDto.username())
                .clickLoginButton();
    }

    @Test
    void register_ShouldShowToastAndNotRedirect_WhenUsernameAlreadyExists() {
        navigateToPage(ControllerUrls.REGISTRATION);
        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        LoginPage loginPage = new LoginPage(getDriver());
        ToastFragment toast = new ToastFragment(getDriver());

        registrationPage.enterUsername(registerDto.username())
                .enterPassword(registerDto.password())
                .enterConfirmPassword(registerDto.password())
                .enterEmail(registerDto.email())
                .clickRegisterButton();

        navigateToPage(ControllerUrls.REGISTRATION);

        registrationPage.enterUsername(registerDto.username())
                .enterPassword(registerDto.password())
                .enterConfirmPassword(registerDto.password())
                .enterEmail(registerDto.email())
                .clickRegisterButton();

        ToastAssert.assertThat(toast).isVisible()
                .hasType(ToastType.ERROR);
    }
}
