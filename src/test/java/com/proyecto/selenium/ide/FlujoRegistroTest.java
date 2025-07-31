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
public void testInicioSesionConCredencialesIncorrectas() {
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

    // Ir a Sign In
    WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign In")));
    signInLink.click();

    wait.until(ExpectedConditions.urlContains("/customer/account/login/"));

    // Completar con credenciales inválidas
    WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
    WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pass")));

    email.sendKeys("correo.incorrecto@example.com");
    password.sendKeys("passwordIncorrecto123!");

    // Click en botón Sign In
    WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("send2")));
    signInButton.click();

    // Esperar mensaje de error por credenciales inválidas
    WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.cssSelector("div.message-error div")));

    // Imprimir mensaje real (útil para debug)
    System.out.println("Mensaje mostrado: " + errorMessage.getText());

    // Validar que el mensaje contiene parte del texto esperado
    Assertions.assertTrue(
        errorMessage.getText().contains("The account sign-in was incorrect"),
        "❌ El mensaje de error no contiene el texto esperado."
    );

    System.out.println("✅ Mensaje de error por credenciales incorrectas mostrado correctamente.");
}

}