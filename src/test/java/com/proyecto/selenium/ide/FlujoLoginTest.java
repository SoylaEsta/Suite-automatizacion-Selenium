package com.proyecto.selenium.ide;

import com.proyecto.selenium.utils.CapturaPantallaUtil;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class FlujoLoginTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    private Map<String, Object> vars;

    @BeforeMethod
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
                driver = new EdgeDriver();
            } else {
                throw new IllegalArgumentException("Navegador no soportado: " + browserType);
            }
            System.out.println("✔ Configuración exitosa.");
        } catch (Exception e) {
            throw new RuntimeException("Error fatal al configurar el navegador", e);
        }

        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void cerrarPopupCookiesSiExiste() {
        try {
            WebElement consentButton = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler")));
            consentButton.click();
        } catch (Exception ignored) {
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

            driver.findElement(By.id("email")).sendKeys("cami.ferrando@gmail.com");
            driver.findElement(By.id("pass")).sendKeys("Camiferrando8");
            driver.findElement(By.id("send2")).click();

            WebElement welcomeMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".logged-in")));
            String mensaje = welcomeMessage.getText();

            Assert.assertTrue(mensaje.contains("Welcome") && mensaje.contains("Camila"),
                "El mensaje de bienvenida no contiene el texto esperado.");

        } catch (Exception e) {
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

            driver.findElement(By.id("email")).sendKeys("usuario.inexistente@gmail.com");
            driver.findElement(By.id("pass")).sendKeys("PasswordIncorrecta");
            driver.findElement(By.id("send2")).click();

            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-error > div")));

            Assert.assertTrue(errorMessage.getText().contains("The account sign-in was incorrect"),
                "No se mostró el mensaje de error esperado.");

        } catch (Exception e) {
            CapturaPantallaUtil.capturar(driver, "fallo_login_fallido");
            throw e;
        }
    }
}
