package com.example.tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class RegisterTestCase {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://1-dot-thoth-web-171921.appspot.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testRegisterTestCase() throws Exception {
    driver.get(baseUrl + "/");
    driver.findElement(By.linkText("Register")).click();
    driver.findElement(By.cssSelector("input.gwt-TextBox")).clear();
    driver.findElement(By.cssSelector("input.gwt-TextBox")).sendKeys("javier paramo");
    driver.findElement(By.xpath("(//input[@type='text'])[2]")).clear();
    driver.findElement(By.xpath("(//input[@type='text'])[2]")).sendKeys("javier@ubu.es");
    driver.findElement(By.cssSelector("input.gwt-PasswordTextBox")).clear();
    driver.findElement(By.cssSelector("input.gwt-PasswordTextBox")).sendKeys("javier");
    driver.findElement(By.cssSelector("button.gwt-Button")).click();
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if ("Registration Success.".equals(driver.findElement(By.cssSelector("div.gwt-HTML")).getText())) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    assertEquals("Registration Success.", driver.findElement(By.cssSelector("div.gwt-HTML")).getText());
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
