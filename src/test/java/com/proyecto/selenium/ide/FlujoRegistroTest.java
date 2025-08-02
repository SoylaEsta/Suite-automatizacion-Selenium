package com.proyecto.selenium.ide;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class FlujoRegistroTest {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


@Test
public void testRecuperacionConFormatoCorreoInvalido() {
    driver.get("https://magento.softwaretestingboard.com/");

    try {
        Thread.sleep(3000);
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("google_vignette")) {
            driver.navigate().back();
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    // Ir a Sign In
    WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign In")));
    signInLink.click();

    wait.until(ExpectedConditions.urlContains("/customer/account/login/"));

    // Ir a Forgot Password
    WebElement forgotPasswordLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Forgot Your Password?")));
    forgotPasswordLink.click();

    wait.until(ExpectedConditions.urlContains("/customer/account/forgotpassword/"));

    // Ingresar correo mal formado
    WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email_address")));
    emailField.sendKeys("correoInvalido"); // sin @

    WebElement submitButton = driver.findElement(By.cssSelector("button.submit"));

    // Usar Javascript para forzar validación del input
    JavascriptExecutor js = (JavascriptExecutor) driver;
    boolean esValido = (Boolean) js.executeScript("return arguments[0].checkValidity();", emailField);

    System.out.println("¿Formato válido? " + esValido);

    Assertions.assertFalse(esValido, "❌ El campo de correo fue considerado válido pese a tener formato incorrecto.");
    System.out.println("✅ El campo de correo con formato inválido fue correctamente rechazado por validación HTML5.");
}



}