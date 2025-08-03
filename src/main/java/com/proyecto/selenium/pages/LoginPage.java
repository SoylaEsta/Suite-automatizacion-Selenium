package com.proyecto.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {
    private WebDriver driver;

    private By emailField = By.id("email");
    private By passwordField = By.id("pass");
    private By loginButton = By.id("send2");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void ingresarEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    public void ingresarPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void hacerClickEnLogin() {
        driver.findElement(loginButton).click();
    }
}
