package com.proyecto.selenium.ide;

import com.proyecto.selenium.utils.CapturaPantallaUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlujoRegistroTest {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // Configura WebDriverManager para Firefox. Esto descarga y gestiona el driver automáticamente.
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();

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

    /*
     * Este archivo corresponde a la Lección 4 del Proyecto:
     * - 3 tipos de localizadores: linkText, id, cssSelector
     * - 2 tipos de espera: WebDriverWait y Thread.sleep
     * - Validación de campos dinámicos con JavascriptExecutor
     * - Captura en caso de fallo (Lección 5)
     */
     
    // Este test está diseñado para pasar, como lo desarrollamos en la Lección 3
    @Test
    public void testRegistroExitoso() {
        try {
            driver.get("https://magento.softwaretestingboard.com/");

            WebElement createAccountLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Create an Account")));
            createAccountLink.click();

            wait.until(ExpectedConditions.urlContains("/customer/account/create/"));

            String uniqueEmail = "reg.valido_" + UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";
            String password = "Password123!";

            WebElement firstNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstname")));
            firstNameField.sendKeys("Test");
            driver.findElement(By.id("lastname")).sendKeys("User");
            driver.findElement(By.id("email_address")).sendKeys(uniqueEmail);
            driver.findElement(By.id("password")).sendKeys(password);
            driver.findElement(By.id("password-confirmation")).sendKeys(password);

            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".submit")));
            submitButton.click();

            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-success > div")));

            Assertions.assertTrue(successMessage.getText().contains("Thank you for registering with Main Website Store."),
                                    "El mensaje de registro exitoso no fue encontrado o es incorrecto.");
            
            System.out.println("Registro exitoso confirmado y verificado para: " + uniqueEmail);
        } catch (AssertionError | Exception e) {
            CapturaPantallaUtil.capturar(driver, "fallo_registro_exitoso");
            throw e;
        }
    }

    @Test
    public void testRegistroFallidoPorCamposVacios() {
        try {
            driver.get("https://magento.softwaretestingboard.com/");
            WebElement createAccountLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Create an Account")));
            js.executeScript("arguments[0].click();", createAccountLink);
            WebElement registerButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.submit")));
            registerButton.click();
            // Corrección: se utiliza el localizador correcto para el mensaje de error del nombre
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstname-error")));
            Assertions.assertTrue(errorMessage.getText().contains("This is a required field."), "El mensaje de error para el campo de nombre no fue encontrado o es incorrecto.");
        } catch (AssertionError | Exception e) {
            CapturaPantallaUtil.capturar(driver, "fallo_registro_vacio");
            throw e;
        }
    }

    @Test
    public void testRegistroConEmailExistente() {
        try {
            driver.get("https://magento.softwaretestingboard.com/");
            WebElement createAccountLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Create an Account")));
            js.executeScript("arguments[0].click();", createAccountLink);

            driver.findElement(By.id("firstname")).sendKeys("Gabriela");
            driver.findElement(By.id("lastname")).sendKeys("Hernandez");
            driver.findElement(By.id("email_address")).sendKeys("gabriela@email.com"); // Correo que ya existe
            driver.findElement(By.id("password")).sendKeys("Password123");
            driver.findElement(By.id("password-confirmation")).sendKeys("Password123");

            driver.findElement(By.cssSelector("button.submit")).click();

            // Corrección: se utiliza el localizador correcto para el mensaje de error general
            WebElement mensajeError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.message-error > div")));
            Assertions.assertTrue(mensajeError.getText().contains("There is already an account with this email address."), "No se mostró el mensaje de error para correo ya registrado.");

        } catch (Exception e) {
            CapturaPantallaUtil.capturar(driver, "fallo_email_existente");
            throw e;
        }
    }

    @Test
    public void testRecuperacionConCamposVacios() {
        try {
            driver.get("https://magento.softwaretestingboard.com/");

            try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
            
            WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign In")));
            signInLink.click();
            wait.until(ExpectedConditions.urlContains("/customer/account/login/"));

            WebElement forgotPasswordLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Forgot Your Password?")));
            forgotPasswordLink.click();
            wait.until(ExpectedConditions.urlContains("/customer/account/forgotpassword/"));

            WebElement submitButton = driver.findElement(By.cssSelector("button.submit"));
            submitButton.click();

            boolean campoRequerido = driver.findElements(By.id("email_address-error")).size() > 0;
            System.out.println("¿Campo requerido? " + campoRequerido);

            // Corrección: se invirtió la aserción de `assertFalse` a `assertTrue`
            Assertions.assertTrue(campoRequerido, "❌ El campo de email no está marcado como obligatorio.");
        } catch (Exception e) {
            CapturaPantallaUtil.capturar(driver, "fallo_campos_vacios");
            throw e;
        }
    }

    @Test
    public void testRecuperacionConFormatoCorreoInvalido() {
        try {
            driver.get("https://magento.softwaretestingboard.com/");

            try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
            
            WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign In")));
            signInLink.click();
            wait.until(ExpectedConditions.urlContains("/customer/account/login/"));

            WebElement forgotPasswordLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Forgot Your Password?")));
            forgotPasswordLink.click();
            wait.until(ExpectedConditions.urlContains("/customer/account/forgotpassword/"));

            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email_address")));
            emailField.sendKeys("correoInvalido");
            WebElement submitButton = driver.findElement(By.cssSelector("button.submit"));

            JavascriptExecutor js = (JavascriptExecutor) driver;
            boolean esValido = (Boolean) js.executeScript("return arguments[0].checkValidity();", emailField);

            System.out.println("¿Formato válido? " + esValido);

            // Corrección: se invirtió la aserción de `assertTrue` a `assertFalse`
            Assertions.assertFalse(esValido, " El campo de correo fue considerado válido pese a tener formato incorrecto.");
            System.out.println(" El campo de correo con formato inválido fue correctamente rechazado por validación HTML5.");
        } catch (AssertionError | Exception e) {
            CapturaPantallaUtil.capturar(driver, "fallo_formatoCorreoInvalido");
            throw e;
        }
    }
}
