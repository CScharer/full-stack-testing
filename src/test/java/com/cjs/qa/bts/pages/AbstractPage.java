package com.cjs.qa.bts.pages;

import com.cjs.qa.core.Environment;
import com.cjs.qa.selenium.ISelenium;
import com.cjs.qa.selenium.Selenium;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AbstractPage implements ISelenium {
  private WebDriver webDriver;
  private String browser;

  protected WebDriver getWebDriver() {
    return webDriver;
  }

  protected String getBrowser() {
    return browser;
  }

  public AbstractPage(WebDriver webDriver) {
    this.webDriver = webDriver;
    if (this.browser == null) {
      final Selenium selenium = new Selenium(webDriver);
      // Default Browser
      this.browser = ISelenium.BROWSER_DEFAULT;
      this.webDriver = selenium.browserProfiling(this.webDriver, this.browser, true);
      this.getWebDriver().get("http://www.Vivit-Worldwide.org");
    }
  }

  protected void selectDropdown(By findBy, String selection) {
    final Select Dropdown = new Select(this.getWebDriver().findElement(findBy));
    Dropdown.selectByVisibleText(selection);
  }

  protected void setEdit(By byLocator, String value) {
    Environment.sysOut("Object:" + byLocator.toString());
    final WebElement element = getWebDriver().findElement(byLocator);
    final WebDriverWait wait = new WebDriverWait(this.webDriver, java.time.Duration.ofSeconds(10));
    wait.until(ExpectedConditions.elementToBeClickable(element));
    Environment.sysOut("Displayed:" + element.isDisplayed());
    Environment.sysOut("Enabled:" + element.isEnabled());
    element.clear();
    element.sendKeys(value);
  }

  public void waitHard(int value) {
    getWebDriver().manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(value));
  }

  protected WebElement waitClickable(WebElement element) {
    final WebDriverWait wait = new WebDriverWait(this.webDriver, java.time.Duration.ofSeconds(10));
    return wait.until(ExpectedConditions.elementToBeClickable(element));
  }

  protected void verifyTitle(String value) {
    final String title = this.getWebDriver().getTitle();
    Assert.assertEquals(value, title);
  }
}
