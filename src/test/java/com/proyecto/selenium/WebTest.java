package com.proyecto.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebTest {

    WebDriver driver;

  @BeforeEach
public void setUp() {
    WebDriverManager.chromedriver().setup();

    ChromeOptions options = new ChromeOptions();
    options.addArguments("--remote-allow-origins=*");
    // NO agregar --headless si quieres ver el navegador
    driver = new ChromeDriver(options);
    }

    @Test
    public void validarTituloDeGoogle() {
        driver.get("https://www.google.com");
        String tituloEsperado = "Google";
        String tituloActual = driver.getTitle();
        assertEquals(tituloEsperado, tituloActual, "El título de la página no es correcto");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
