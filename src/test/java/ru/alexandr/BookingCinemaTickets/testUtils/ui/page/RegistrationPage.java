package ru.alexandr.BookingCinemaTickets.testUtils.ui.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.alexandr.BookingCinemaTickets.testUtils.constant.WaitConstant;
import jakarta.annotation.Nonnull;

public class RegistrationPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "phoneNumber")
    private WebElement phoneNumberInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "confirmPassword")
    private WebElement confirmPasswordInput;

    @FindBy(id = "register-button")
    private WebElement registerButton;

    @FindBy(id = "login-link")
    private WebElement loginLink;

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, WaitConstant.DEFAULT_WAIT);
        PageFactory.initElements(driver, this);
    }

    public RegistrationPage enterUsername(@Nonnull String username) {
        wait.until(ExpectedConditions.visibilityOf(usernameInput)).sendKeys(username);
        return this;
    }

    public RegistrationPage enterEmail(@Nonnull String email) {
        wait.until(ExpectedConditions.visibilityOf(emailInput)).sendKeys(email);
        return this;
    }

    public RegistrationPage enterPhoneNumber(@Nonnull String phoneNumber) {
        wait.until(ExpectedConditions.visibilityOf(phoneNumberInput)).sendKeys(phoneNumber);
        return this;
    }

    public RegistrationPage enterPassword(@Nonnull String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordInput)).sendKeys(password);
        return this;
    }

    public RegistrationPage enterConfirmPassword(@Nonnull String confirmPassword) {
        wait.until(ExpectedConditions.visibilityOf(confirmPasswordInput)).sendKeys(confirmPassword);
        return this;
    }

    public RegistrationPage clickRegisterButton() {
        wait.until(ExpectedConditions.elementToBeClickable(registerButton)).click();
        return this;
    }

    public RegistrationPage clickLoginLink() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
        return this;
    }
}
