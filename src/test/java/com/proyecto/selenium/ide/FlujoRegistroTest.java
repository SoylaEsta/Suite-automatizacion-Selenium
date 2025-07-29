package com.proyecto.selenium.ide;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
// Importaciones para WebDriverWait y ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.github.bonigarcia.wdm.WebDriverManager; // Importación para WebDriverManager

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration; // Para usar Duration con WebDriverWait
import java.util.HashMap;
import java.util.Map;

public class FlujoRegistroTest {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;
    private WebDriverWait wait; // Declarar WebDriverWait

    @BeforeEach
    public void setUp() {
        // Configura WebDriverManager para Firefox. Esto descarga y gestiona el driver automáticamente.
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();
        // Inicializar WebDriverWait con un tiempo de espera máximo (aumentado a 30 segundos)
        wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Aumentado a 30 segundos
        driver.manage().window().maximize(); // Maximizar la ventana para asegurar visibilidad
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) { // Asegurarse de que el driver no sea null antes de cerrar
            driver.quit();
        }
    }

    @Test
    public void testRegistro() {
        driver.get("https://magento.softwaretestingboard.com/");
        
        // Esperar a que el enlace "Create an Account" sea clickeable
        WebElement createAccountLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Create an Account")));
        createAccountLink.click();

        // **NUEVO:** Esperar a que la URL cambie a la página de registro
        // La URL de registro de Magento es generalmente /customer/account/create/
        wait.until(ExpectedConditions.urlContains("/customer/account/create/")); 
        
        // Esperar a que el campo 'firstname' sea visible y clickeable
        // Esto es crucial porque la página tiene un pequeño retraso en la carga del formulario.
        WebElement firstNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstname")));
        firstNameField.sendKeys("Camila");

        // El resto de los campos deberían estar listos después de esperar por el primero
        driver.findElement(By.id("lastname")).sendKeys("Ferrando");
        driver.findElement(By.id("email_address")).sendKeys("cami.ferrando@gmail.com");
        driver.findElement(By.id("password")).sendKeys("Camiferrando8");
        driver.findElement(By.id("password-confirmation")).sendKeys("Camiferrando8");

        // Esperar a que el botón de submit sea clickeable
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".submit")));
        submitButton.click();

        // Puedes añadir una aserción aquí para verificar que el registro fue exitoso
        // Por ejemplo, esperar a que aparezca un mensaje de éxito o que el usuario sea redirigido.
        // wait.until(ExpectedConditions.urlContains("customer/account/"));
        // wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-success")));
        System.out.println("Intento de registro completado. Verifica manualmente si fue exitoso.");
    }
}