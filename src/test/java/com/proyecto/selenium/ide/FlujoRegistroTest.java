package com.proyecto.selenium.ide;

import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.UUID;

public class FlujoRegistroTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    @Parameters("browser")
    @BeforeMethod
    public void setUp(@Optional("firefox") String browserType) {
        System.out.println("Iniciando pruebas en el navegador: " + browserType);

        try {
            if (browserType.equalsIgnoreCase("chrome")) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            } else if (browserType.equalsIgnoreCase("firefox")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            } else if (browserType.equalsIgnoreCase("edge")) {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
            } else {
                throw new IllegalArgumentException("Navegador no soportado: " + browserType);
            }
            System.out.println("✔ Driver configurado correctamente.");
        } catch (Exception e) {
            System.err.println("❌ Error al configurar el driver: " + e.getMessage());
            throw new RuntimeException(e);
        }

        js = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (!result.isSuccess()) {
            capturarPantalla(result.getName());
        }
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testRegistroExitoso() {
        try {
            driver.get("https://magento.softwaretestingboard.com/");
            cerrarPopupCookiesSiExiste();

            WebElement createAccountLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Create an Account")));
            createAccountLink.click();

            wait.until(ExpectedConditions.urlContains("/customer/account/create/"));

            String uniqueEmail = "gabriela" + UUID.randomUUID().toString().substring(0, 8) + "@mail.com";
            String password = "Password123!";

            driver.findElement(By.id("firstname")).sendKeys("Gabriela");
            driver.findElement(By.id("lastname")).sendKeys("Perez");
            driver.findElement(By.id("email_address")).sendKeys(uniqueEmail);
            driver.findElement(By.id("password")).sendKeys(password);
            driver.findElement(By.id("password-confirmation")).sendKeys(password);

            driver.findElement(By.cssSelector("button[title='Create an Account']")).click();

            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-success > div")));

            Assert.assertTrue(
                successMessage.getText().contains("Thank you for registering with Main Website Store."),
                "El mensaje de éxito no fue encontrado"
            );

            System.out.println("✔ Registro exitoso verificado.");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void testRegistroConEmailExistente() {
        try {
            driver.get("https://magento.softwaretestingboard.com/");
            cerrarPopupCookiesSiExiste();

            WebElement createAccountLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Create an Account")));
            createAccountLink.click();

            wait.until(ExpectedConditions.urlContains("/customer/account/create/"));

            driver.findElement(By.id("firstname")).sendKeys("Test");
            driver.findElement(By.id("lastname")).sendKeys("User");

            driver.findElement(By.id("email_address")).sendKeys("cami.ferrando@gmail.com"); // Email ya existente
            driver.findElement(By.id("password")).sendKeys("Password123!");
            driver.findElement(By.id("password-confirmation")).sendKeys("Password123!");

            driver.findElement(By.cssSelector("button[title='Create an Account']")).click();

            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-error > div")));

            Assert.assertTrue(
                errorMessage.getText().contains("There is already an account with this email address."),
                "El mensaje de error no fue encontrado"
            );

            System.out.println("✔ Registro fallido con email existente verificado.");
        } catch (Exception e) {
            throw e;
        }
    }

    private void cerrarPopupCookiesSiExiste() {
        try {
            WebElement cookies = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler")));
            cookies.click();
            System.out.println("✔ Pop-up de cookies cerrado.");
        } catch (TimeoutException e) {
            System.out.println("ℹ No se encontró pop-up de cookies.");
        }
    }

    @Attachment(value = "Captura de pantalla", type = "image/png")
    public byte[] capturarPantalla(String nombreTest) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destino = new File("screenshots/" + nombreTest + ".png");
        try {
            Files.createDirectories(destino.getParentFile().toPath());
            Files.copy(screenshot.toPath(), destino.toPath());
            return Files.readAllBytes(destino.toPath());
        } catch (IOException e) {
            System.err.println("❌ No se pudo guardar la captura: " + e.getMessage());
            return new byte[0];
        }
    }
}
