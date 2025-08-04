package com.proyecto.selenium.ide;

import io.github.bonigarcia.wdm.WebDriverManager;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;

public class FlujoLoginConDatosTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @Parameters("browser")
    @BeforeMethod
    public void setUp(@Optional("firefox") String browser) {
        System.out.println("Iniciando pruebas en el navegador: " + browser);
        try {
            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver();
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    break;
                default:
                    throw new IllegalArgumentException("Navegador no soportado: " + browser);
            }
            System.out.println("✔ Configuración del driver de " + browser + " exitosa.");
        } catch (Exception e) {
            System.err.println("❌ Error al configurar el driver del navegador: " + e.getMessage());
            throw new RuntimeException(e);
        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();
        driver.get("https://magento.softwaretestingboard.com/customer/account/login/");
    }

    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {
        return new Object[][] {
                {"camila@gmail.com", "123456", "Welcome, Camila Ferrando!"}
        };
    }

    @Test(dataProvider = "loginData")
    public void testLoginConDatos(String email, String password, String expectedWelcomeText) {
        removerOverlaySiExiste();

        WebElement campoEmail = wait.until(ExpectedConditions.elementToBeClickable(By.id("email")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", campoEmail);
        campoEmail.clear();
        campoEmail.sendKeys(email);

        WebElement campoPassword = wait.until(ExpectedConditions.elementToBeClickable(By.id("pass")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", campoPassword);
        campoPassword.clear();
        campoPassword.sendKeys(password);

        WebElement botonLogin = wait.until(ExpectedConditions.elementToBeClickable(By.id("send2")));
        botonLogin.click();

        WebElement mensajeBienvenida = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".logged-in")));
        String actualWelcomeText = mensajeBienvenida.getText().trim();

        Assert.assertEquals(actualWelcomeText, expectedWelcomeText, "El mensaje de bienvenida no coincide");
    }

    private void removerOverlaySiExiste() {
        try {
            WebElement cookieConsent = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler")));
            cookieConsent.click();
        } catch (TimeoutException ignored) {
            // Overlay no está presente, continuar con el flujo.
        }
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

    @Attachment(value = "Captura de pantalla", type = "image/png")
    public byte[] capturarPantalla(String nombreTest) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destino = new File("screenshots/" + nombreTest + ".png");
        try {
            Files.createDirectories(destino.getParentFile().toPath());
            Files.copy(screenshot.toPath(), destino.toPath());
            return Files.readAllBytes(destino.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
