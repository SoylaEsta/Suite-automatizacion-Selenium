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

public class FlujoRegistroTest {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // Lee la propiedad del sistema "browser". Si no se especifica, usa "firefox" por defecto.
        String browserType = System.getProperty("browser", "firefox");
        System.out.println("Iniciando pruebas en el navegador: " + browserType);
        
        try {
            if ("chrome".equalsIgnoreCase(browserType)) {
                // Configura WebDriverManager para Chrome
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            } else if ("firefox".equalsIgnoreCase(browserType)) {
                // Configura WebDriverManager para Firefox (opción por defecto)
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            } else if ("edge".equalsIgnoreCase(browserType)) {
                // Configuración de la ruta del driver de Edge directamente en el código.
                // Esto evita problemas de sintaxis con el comando de Maven.
                System.setProperty("webdriver.edge.driver", "C:\\msedgedriver.exe");
                System.out.println("Usando el driver de Edge local en: C:\\msedgedriver.exe");
                driver = new EdgeDriver();
            } else {
                throw new IllegalArgumentException("Navegador no soportado: " + browserType);
            }
            System.out.println("✔ Configuración del driver de " + browserType + " exitosa.");
        } catch (Exception e) {
            System.err.println("❌ Error fatal al configurar el driver del navegador: " + browserType + ". Causa: " + e.getMessage());
            throw new RuntimeException("Error fatal al configurar el driver del navegador: " + browserType, e);
        }

        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();

        // Manejo del pop-up de cookies y otros posibles overlays
        // Este bloque es crucial para evitar el error 'ElementClickInterceptedException'.
        // Espera de forma flexible el botón de aceptar cookies o cualquier pop-up.
        try {
            WebDriverWait consentWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement consentButton = consentWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button#onetrust-accept-btn-handler, div[class*='consent'] button")));
            consentButton.click();
            System.out.println("✔ Pop-up de cookies cerrado.");
        } catch (Exception e) {
            System.out.println("ℹ No se encontró pop-up de cookies.");
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testRegistroExitoso() {
        try {
            driver.get("https://magento.softwaretestingboard.com/");

            // Click en "Create an Account"
            WebElement createAccountLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Create an Account")));
            createAccountLink.click();

            wait.until(ExpectedConditions.urlContains("/customer/account/create/"));

            // Llenar el formulario de registro con datos válidos
            driver.findElement(By.id("firstname")).sendKeys("Gabriela");
            driver.findElement(By.id("lastname")).sendKeys("Perez");
            // Genera un email aleatorio para cada ejecución
            String randomEmail = "gabriela.perez" + System.currentTimeMillis() + "@gmail.com";
            driver.findElement(By.id("email_address")).sendKeys(randomEmail);
            driver.findElement(By.id("password")).sendKeys("Password123!");
            driver.findElement(By.id("password-confirmation")).sendKeys("Password123!");

            // Click en el botón "Create an Account"
            driver.findElement(By.cssSelector("button[title='Create an Account']")).click();

            // Verificar el mensaje de éxito de registro
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-success > div")));
            Assertions.assertTrue(successMessage.getText().contains("Thank you for registering with Main Website Store."),
                                "El mensaje de registro exitoso no fue encontrado o es incorrecto.");
            
            System.out.println("✔ Registro exitoso confirmado.");
        } catch (AssertionError | Exception e) {
            CapturaPantallaUtil.capturar(driver, "fallo_registro_exitoso");
            throw e;
        }
    }

    @Test
    public void testRegistroConEmailExistente() {
        try {
            driver.get("https://magento.softwaretestingboard.com/");
            
            // Click en "Create an Account"
            WebElement createAccountLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Create an Account")));
            createAccountLink.click();

            wait.until(ExpectedConditions.urlContains("/customer/account/create/"));
            
            // Llenar el formulario con un email existente
            driver.findElement(By.id("firstname")).sendKeys("Test");
            driver.findElement(By.id("lastname")).sendKeys("User");
            driver.findElement(By.id("email_address")).sendKeys("cami.ferrando@gmail.com"); // Email ya registrado
            driver.findElement(By.id("password")).sendKeys("Password123!");
            driver.findElement(By.id("password-confirmation")).sendKeys("Password123!");
            
            // Click en el botón "Create an Account"
            driver.findElement(By.cssSelector("button[title='Create an Account']")).click();

            // Verificar que se muestra el mensaje de error de email existente
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-error > div")));
            Assertions.assertTrue(errorMessage.getText().contains("There is already an account with this email address."),
                                    "El mensaje de error de email existente no fue encontrado o es incorrecto.");
            
            System.out.println("✔ Registro fallido con email existente verificado.");
        } catch (AssertionError | Exception e) {
            CapturaPantallaUtil.capturar(driver, "fallo_registro_con_email_existente");
            throw e;
        }
    }
}
