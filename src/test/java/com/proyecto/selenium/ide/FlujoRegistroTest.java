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
public void testRegistroExitoso() {
    driver.get("https://magento.softwaretestingboard.com/");

    // Manejo de posibles anuncios intermedios
    try {
        Thread.sleep(3000);
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("google_vignette")) {
            driver.navigate().back();
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    // Ir a Create an Account
    WebElement createAccountLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Create an Account")));
    createAccountLink.click();

    wait.until(ExpectedConditions.urlContains("/customer/account/create/"));

    // Completar campos obligatorios con datos válidos
    WebElement firstName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstname")));
    WebElement lastName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lastname")));
    WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email_address")));
    WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
    WebElement confirmPassword = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password-confirmation")));

    firstName.sendKeys("Gabriela");
    lastName.sendKeys("Hernández");
    // Para email, usar uno único o con timestamp para evitar colisiones en pruebas repetidas
    String emailUnico = "gabriela" + System.currentTimeMillis() + "@example.com";
    email.sendKeys(emailUnico);
    password.sendKeys("Password123!");
    confirmPassword.sendKeys("Password123!");

    // Click en botón Create an Account
    WebElement createAccountButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[title='Create an Account']")));
    createAccountButton.click();

    // Esperar mensaje o redirección que confirme registro exitoso
    // En Magento normalmente aparece un mensaje con clase .message-success o se redirige al dashboard
    WebElement mensajeExito = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-success")));

    Assertions.assertTrue(mensajeExito.isDisplayed(), "❌ No se mostró mensaje de éxito tras el registro.");
    Assertions.assertTrue(mensajeExito.getText().toLowerCase().contains("thank you for registering"),
        "❌ El mensaje de éxito no contiene el texto esperado.");

    System.out.println("✅ Registro exitoso confirmado.");
}

}