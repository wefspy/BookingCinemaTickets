package ru.alexandr.BookingCinemaTickets.testUtils.ui.page;

import jakarta.annotation.Nonnull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.alexandr.BookingCinemaTickets.testUtils.constant.WaitConstant;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(id = "register-link")
    private WebElement registerLink;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, WaitConstant.DEFAULT_WAIT);
        PageFactory.initElements(driver, this);
    }

    public LoginPage enterUsername(@Nonnull String username) {
        wait.until(ExpectedConditions.visibilityOf(usernameInput)).sendKeys(username);
        return this;
    }

    public LoginPage enterPassword(@Nonnull String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordInput)).sendKeys(password);
        return this;
    }

    public LoginPage clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        return this;
    }

    public LoginPage clickRegisterLink() {
        wait.until(ExpectedConditions.elementToBeClickable(registerLink)).click();
        return this;
    }
}
