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

    try {
        Thread.sleep(3000); // Esperar posible aparición de intersticial
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("google_vignette")) {
            driver.navigate().back();
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    WebElement createAccountLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Create an Account")));
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("arguments[0].click();", createAccountLink);



    // Esperar a que la página cargue
    try {
        Thread.sleep(2000); // Espera breve para que termine animación/redirección
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    // Verificar nuevamente si se redirigió a un intersticial
    String currentUrl = driver.getCurrentUrl();
    if (currentUrl.contains("google_vignette")) {
        driver.navigate().back(); // Volver atrás
        createAccountLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Create an Account")));
        createAccountLink.click(); // Volver a intentar
    }

    // Confirmar que se abrió la página de creación de cuenta
    wait.until(ExpectedConditions.urlContains("/customer/account/create/"));

    // Generar email dinámico
    String email = "gabriela" + System.currentTimeMillis() + "@mailinator.com";

    // Completar formulario de registro
    driver.findElement(By.id("firstname")).sendKeys("Gabriela");
    driver.findElement(By.id("lastname")).sendKeys("Test");
    driver.findElement(By.id("email_address")).sendKeys(email);
    driver.findElement(By.id("password")).sendKeys("Clave123!");
    driver.findElement(By.id("password-confirmation")).sendKeys("Clave123!");

    // Enviar formulario
    WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".submit")));
    submitButton.click();

    // Validar mensaje de éxito
    WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.cssSelector(".message-success.success.message")));

    Assertions.assertTrue(
        successMsg.getText().contains("Thank you for registering") ||
        driver.getCurrentUrl().contains("/customer/account/"),
        "No se encontró el mensaje de registro exitoso."
    );

    System.out.println("✅ Registro exitoso validado correctamente.");
}
}