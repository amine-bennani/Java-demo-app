package com.example.demo;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;

public class SeleniumFormTest {

    static WebDriver driver;

    @BeforeAll
    public static void setup() {
        // Assumes chromedriver is installed in PATH
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterAll
    public static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSubmitForm() {
        driver.get("http://localhost:30080/submit");

        driver.findElement(By.name("name")).sendKeys("John Doe");
        driver.findElement(By.name("email")).sendKeys("john@example.com");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement msg = driver.findElement(By.cssSelector("p"));
        Assertions.assertTrue(msg.getText().contains("Thank you"), "Confirmation message missing");
    }

    @Test
    public void testLoginForm() {
        driver.get("http://localhost:30080/login");

        driver.findElement(By.name("username")).sendKeys("testuser");
        driver.findElement(By.name("password")).sendKeys("password123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        Assertions.assertFalse(driver.getPageSource().contains("Login failed"), "Login failed unexpectedly");
    }
}
