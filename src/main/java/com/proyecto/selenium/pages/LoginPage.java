package com.proyecto.selenium.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By emailField = By.id("email");
    private By passwordField = By.id("pass");
    private By signInButton = By.id("send2");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(30));
    }

    public void ingresarEmail(String email) {
        try {
            WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(emailField));
            scrollIntoView(emailInput);
            emailInput.clear();
            emailInput.sendKeys(email);
        } catch (ElementNotInteractableException e) {
            ejecutarSendKeysPorJS(emailField, email);
        }
    }

    public void ingresarPassword(String password) {
        try {
            WebElement passInput = wait.until(ExpectedConditions.elementToBeClickable(passwordField));
            scrollIntoView(passInput);
            passInput.clear();
            passInput.sendKeys(password);
        } catch (ElementNotInteractableException e) {
            ejecutarSendKeysPorJS(passwordField, password);
        }
    }

    public void clickLogin() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(signInButton));
        scrollIntoView(button);
        button.click();
    }

    private void ejecutarSendKeysPorJS(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        scrollIntoView(element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", element, value);
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }
}
