package com.proyecto.selenium.ide;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;

import java.util.HashMap;
import java.util.Map;

public class FlujoLoginTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;

  @BeforeEach
  public void setUp() {
    driver = new FirefoxDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<>();
  }

  @AfterEach
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void testLogin() {
    driver.get("https://magento.softwaretestingboard.com/");
    driver.manage().window().setSize(new Dimension(1382, 744));
    WebElement signIn = driver.findElement(By.linkText("Sign In"));
    new Actions(driver).moveToElement(signIn).perform();
    signIn.click();
    driver.findElement(By.id("email")).sendKeys("cami.ferrando@gmail.com");
    driver.findElement(By.id("pass")).sendKeys("Camiferrando8");
    driver.findElement(By.cssSelector(".primary:nth-child(1) > #send2 > span")).click();
  }
}
