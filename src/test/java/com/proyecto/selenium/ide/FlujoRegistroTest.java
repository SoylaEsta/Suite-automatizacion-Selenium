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
public void testRegistroConCamposVacios() {
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

    // Hacer foco breve en los campos obligatorios (simular interacción mínima)
    WebElement firstName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstname")));
    WebElement lastName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lastname")));
    WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email_address")));
    WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
    WebElement confirmPassword = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password-confirmation")));

    // Simular foco y blur (TAB) en todos los campos
    firstName.click(); firstName.sendKeys(Keys.TAB);
    lastName.click(); lastName.sendKeys(Keys.TAB);
    email.click(); email.sendKeys(Keys.TAB);
    password.click(); password.sendKeys(Keys.TAB);
    confirmPassword.click(); confirmPassword.sendKeys(Keys.TAB);

    // Enviar formulario vacío
    WebElement createAccountButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[title='Create an Account']")));
    createAccountButton.click();

    // Esperar mensaje de error por campo vacío
    WebElement errorField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstname-error")));

    Assertions.assertTrue(errorField.isDisplayed(), "❌ El mensaje de error no se muestra para el campo First Name.");
    System.out.println("✅ Se mostró mensaje de validación para campos vacíos.");
}



}