package com.proyecto.selenium.ide;

import com.proyecto.selenium.utils.CapturaPantallaUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class FlujoLoginTest {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        String browserType = System.getProperty("browser", "firefox");
        System.out.println("Iniciando pruebas en el navegador: " + browserType);

        try {
            if ("chrome".equalsIgnoreCase(browserType)) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            } else if ("firefox".equalsIgnoreCase(browserType)) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            } else if ("edge".equalsIgnoreCase(browserType)) {
                System.setProperty("webdriver.edge.driver", "C:\\msedgedriver.exe");
                System.out.println("Usando el driver de Edge local en: C:\\msedgedriver.exe");
                driver = new EdgeDriver();
            } else {
                throw new IllegalArgumentException("Navegador no soportado: " + browserType);
            }
            System.out.println("✔ Configuración del driver de " + browserType + " exitosa.");
        } catch (Exception e) {
            System.err.println("❌ Error al configurar el driver: " + e.getMessage());
            throw new RuntimeException("Error fatal al configurar el navegador", e);
        }

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

    private void cerrarPopupCookiesSiExiste() {
        try {
            WebDriverWait consentWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement consentButton = consentWait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button#onetrust-accept-btn-handler, div[class*='consent'] button")
                )
            );
            consentButton.click();
            System.out.println("✔ Pop-up de cookies cerrado.");
        } catch (Exception e) {
            System.out.println("ℹ No se encontró pop-up de cookies.");
        }
    }

    @Test
    public void testLoginExitoso() {
        try {
            driver.get("https://magento.softwaretestingboard.com/");
            cerrarPopupCookiesSiExiste();

            WebElement signIn = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign In")));
            signIn.click();

            wait.until(ExpectedConditions.urlContains("/customer/account/login/"));

            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
            emailField.sendKeys("cami.ferrando@gmail.com");

            driver.findElement(By.id("pass")).sendKeys("Camiferrando8");
            driver.findElement(By.cssSelector(".primary:nth-child(1) > #send2 > span")).click();

            WebElement welcomeMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".logged-in")));
            String mensaje = welcomeMessage.getText();
            Assertions.assertTrue(mensaje.contains("Welcome") && mensaje.contains("Camila"),
                "❌ El mensaje de bienvenida no contiene el texto esperado. Mensaje real: " + mensaje);

            System.out.println("✔ Inicio de sesión exitoso confirmado.");
        } catch (AssertionError | Exception e) {
            CapturaPantallaUtil.capturar(driver, "fallo_login_exitoso");
            throw e;
        }
    }

    @Test
    public void testLoginFallido() {
        try {
            driver.get("https://magento.softwaretestingboard.com/");
            cerrarPopupCookiesSiExiste();

            WebElement signIn = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign In")));
            signIn.click();

            wait.until(ExpectedConditions.urlContains("/customer/account/login/"));

            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
            emailField.sendKeys("usuario.inexistente@gmail.com");

            driver.findElement(By.id("pass")).sendKeys("PasswordIncorrecta");
            driver.findElement(By.cssSelector(".primary:nth-child(1) > #send2 > span")).click();

            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-error > div")));
            Assertions.assertTrue(errorMessage.getText().contains("The account sign-in was incorrect"),
                "El mensaje de error de login fallido no fue encontrado o es incorrecto.");

            System.out.println("✔ Inicio de sesión fallido con credenciales incorrectas verificado.");
        } catch (AssertionError | Exception e) {
            CapturaPantallaUtil.capturar(driver, "fallo_login_fallido");
            throw e;
        }
    }
}
