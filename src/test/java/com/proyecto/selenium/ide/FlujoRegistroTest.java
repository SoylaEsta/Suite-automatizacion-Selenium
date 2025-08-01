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
public void testFlujoRegistroYLoginExitoso() {
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

    // Hacer clic en "Create an Account"
    WebElement createAccountLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Create an Account")));
    createAccountLink.click();
    wait.until(ExpectedConditions.urlContains("/customer/account/create/"));

    // Generar email único
    String email = "gabriela" + System.currentTimeMillis() + "@mailinator.com";

    // Llenar formulario de registro
    driver.findElement(By.id("firstname")).sendKeys("Gabriela");
    driver.findElement(By.id("lastname")).sendKeys("Test");
    driver.findElement(By.id("email_address")).sendKeys(email);
    driver.findElement(By.id("password")).sendKeys("Clave123!");
    driver.findElement(By.id("password-confirmation")).sendKeys("Clave123!");

    WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".submit")));
    submitButton.click();

    // Validar mensaje de éxito tras registro
    WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.cssSelector(".message-success.success.message")));
    Assertions.assertTrue(successMsg.getText().contains("Thank you for registering"),
        "❌ No se encontró el mensaje de registro exitoso.");

    System.out.println("✅ Registro exitoso validado correctamente.");

    // Cerrar sesión
    WebElement accountMenu = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".customer-name")));
    accountMenu.click();

    WebElement signOutLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign Out")));
    signOutLink.click();

    wait.until(ExpectedConditions.urlContains("/customer/account/logoutSuccess/"));

    // Iniciar sesión con las credenciales recién creadas
    WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign In")));
    signInLink.click();

    wait.until(ExpectedConditions.urlContains("/customer/account/login/"));

    WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
    WebElement passInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pass")));

    emailInput.sendKeys(email);
    passInput.sendKeys("Clave123!");

    WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("send2")));
    signInButton.click();

    // Esperar a que cargue la página correctamente
    try {
        Thread.sleep(3000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    // Validar mensaje de bienvenida (usuario logueado)
    WebElement welcomeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.cssSelector(".panel.header .greet.welcome")));
    String welcomeText = welcomeElement.getText();

    System.out.println("Texto de bienvenida encontrado: " + welcomeText);

    Assertions.assertTrue(welcomeText.contains("Welcome"),
        "❌ No se mostró mensaje de bienvenida tras login.");
    System.out.println("✅ Flujo completo de registro e inicio de sesión validado correctamente.");
}


}