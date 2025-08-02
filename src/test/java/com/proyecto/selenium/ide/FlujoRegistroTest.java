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
public void testRecuperacionConCorreoNoRegistrado() {
    driver.get("https://magento.softwaretestingboard.com/");

    // Manejo de posibles anuncios
    try {
        Thread.sleep(3000);
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("google_vignette")) {
            driver.navigate().back();
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    // Ir a "Sign In"
    WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign In")));
    signInLink.click();

    // Hacer clic en "Forgot Your Password?"
    WebElement forgotPasswordLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Forgot Your Password?")));
    forgotPasswordLink.click();

    wait.until(ExpectedConditions.urlContains("/customer/account/forgotpassword"));

    // Ingresar correo no registrado
    WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email_address")));
    emailField.sendKeys("noexiste123456@mailinator.com");

    // Click en botón "Reset My Password"
    WebElement resetButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.action.submit.primary")));
    resetButton.click();

    // Esperar mensaje de confirmación
    WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("div[data-bind*='prepareMessageForHtml']")));

    // Validar contenido del mensaje
    String expectedText = "If there is an account associated with noexiste123456@mailinator.com you will receive an email with a link to reset your password.";
    Assertions.assertTrue(message.getText().contains(expectedText), "❌ No se mostró el mensaje esperado para correo no registrado.");

    System.out.println("✅ Mensaje de recuperación para correo no registrado validado correctamente.");
}

}