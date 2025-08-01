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
public void testInicioSesionExitoso() {
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

    WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign In")));
    signInLink.click();

    wait.until(ExpectedConditions.urlContains("/customer/account/login/"));

    WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
    WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pass")));

    email.sendKeys("cami.ferrando@gmail.com");
    password.sendKeys("Camiferrando8");

    WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("send2")));
    signInButton.click();

    // Esperar a que aparezca el mensaje y volver a buscar el elemento antes de obtener el texto
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".logged-in")));
    WebElement welcomeMsg = driver.findElement(By.cssSelector(".logged-in"));
    String welcomeText = welcomeMsg.getText();

    System.out.println("Mensaje mostrado: " + welcomeText);

    Assertions.assertTrue(welcomeText.contains("Welcome"), "❌ No se mostró mensaje de bienvenida tras inicio de sesión.");
    System.out.println("✅ Inicio de sesión exitoso validado correctamente.");
}

}