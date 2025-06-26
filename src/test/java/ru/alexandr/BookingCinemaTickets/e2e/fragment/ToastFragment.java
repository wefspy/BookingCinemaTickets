package ru.alexandr.BookingCinemaTickets.e2e.fragment;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.alexandr.BookingCinemaTickets.controller.web.enums.ToastType;
import ru.alexandr.BookingCinemaTickets.testUtils.constant.WaitConstant;

import java.time.Duration;

public class ToastFragment {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "toast-notification")
    private WebElement toast;

    @FindBy(id = "toast-notification-ms-text")
    private WebElement message;

    @FindBy(id = "toast-notification-close-btn")
    private WebElement closeButton;

    public ToastFragment(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, WaitConstant.DEFAULT_WAIT);
        PageFactory.initElements(driver, this);
    }

    public boolean isVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOf(toast));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getMessage() {
        return wait.until(ExpectedConditions.visibilityOf(message)).getText();
    }

    public void clickCloseButton() {
        wait.until(ExpectedConditions.visibilityOf(closeButton)).click();
    }

    public ToastType getType() {
        wait.until(ExpectedConditions.visibilityOf(toast));
        String type = toast.getAttribute("data-type");
        return ToastType.valueOfByColor(type);
    }
}
