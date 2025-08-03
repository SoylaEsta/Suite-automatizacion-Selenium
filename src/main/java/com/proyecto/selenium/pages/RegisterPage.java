package com.proyecto.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RegisterPage {
    private WebDriver driver;

    private By firstName = By.id("firstname");
    private By lastName = By.id("lastname");
    private By email = By.id("email_address");
    private By password = By.id("password");
    private By confirmPassword = By.id("password-confirmation");
    private By createAccountButton = By.cssSelector("button[title='Create an Account']");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    public void completarFormulario(String nombre, String apellido, String correo, String clave) {
        driver.findElement(firstName).sendKeys(nombre);
        driver.findElement(lastName).sendKeys(apellido);
        driver.findElement(email).sendKeys(correo);
        driver.findElement(password).sendKeys(clave);
        driver.findElement(confirmPassword).sendKeys(clave);
    }

    public void clickCrearCuenta() {
        driver.findElement(createAccountButton).click();
    }
}
