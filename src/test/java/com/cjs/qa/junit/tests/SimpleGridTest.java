package com.cjs.qa.junit.tests;

import com.cjs.qa.utilities.AllureHelper;
import io.qameta.allure.*;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

/** Simple TestNG test to verify Grid connection and basic Selenium functionality */
@Epic("Selenium Grid Testing")
@Feature("Basic Grid Tests")
public class SimpleGridTest {
  private static final Logger LOGGER = LogManager.getLogger(SimpleGridTest.class);

  private WebDriver driver;
  private String gridUrl;

  @BeforeMethod
  public void setUp() throws Exception {
    // Check if running in Docker (SELENIUM_REMOTE_URL environment variable)
    gridUrl = System.getenv("SELENIUM_REMOTE_URL");
    if (gridUrl == null || gridUrl.isEmpty()) {
      gridUrl = "http://localhost:4444/wd/hub";
    }

    LOGGER.info("========================================");
    LOGGER.info("Connecting to Grid at: {}", gridUrl);
    LOGGER.info("========================================");

    ChromeOptions options = new ChromeOptions();

    // Check if headless mode is requested (default: true)
    String headlessProperty = System.getProperty("headless", "true");
    boolean isHeadless = !"false".equalsIgnoreCase(headlessProperty);

    if (isHeadless) {
      options.addArguments("--headless");
    }
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--disable-gpu");

    driver = new RemoteWebDriver(URI.create(gridUrl).toURL(), options);
    LOGGER.info("✅ Driver initialized in {} mode", isHeadless ? "headless" : "headed");
    LOGGER.info("✅ Successfully connected to Grid!");
  }

  @Test(priority = 1)
  @Story("Grid Connection")
  @Severity(SeverityLevel.BLOCKER)
  @Description("Verify that connection to Selenium Grid is successful and driver is initialized")
  public void testGridConnection() {
    LOGGER.info("\n>>> Running testGridConnection");
    Allure.step("Verify driver is not null");
    Assert.assertNotNull(driver, "Driver should be initialized");
    LOGGER.info("✅ Grid connection test PASSED\n");
  }

  @Test(priority = 2)
  @Story("Basic Navigation")
  @Severity(SeverityLevel.CRITICAL)
  @Description("Verify navigation to Google.com works correctly")
  public void testNavigateToGoogle() throws Exception {
    LOGGER.info("\n>>> Running testNavigateToGoogle");
    LOGGER.info("Navigating to Google...");
    Allure.step("Navigate to Google.com");
    driver.get("https://www.google.com");

    String title = driver.getTitle();
    LOGGER.info("Page title: {}", title);

    Assert.assertTrue(title.contains("Google"), "Title should contain 'Google'");
    LOGGER.info("✅ Google navigation test PASSED\n");
  }

  @Test(priority = 3)
  @Story("Basic Navigation")
  @Severity(SeverityLevel.CRITICAL)
  @Description("Verify navigation to GitHub.com works correctly")
  public void testNavigateToGitHub() throws Exception {
    LOGGER.info("\n>>> Running testNavigateToGitHub");
    LOGGER.info("Navigating to GitHub...");
    Allure.step("Navigate to GitHub.com");
    driver.get("https://github.com");

    String title = driver.getTitle();
    LOGGER.info("Page title: {}", title);

    Assert.assertTrue(title.contains("GitHub"), "Title should contain 'GitHub'");
    LOGGER.info("✅ GitHub navigation test PASSED\n");
  }

  @AfterMethod
  public void tearDown(ITestResult result) {
    if (driver != null) {
      // Capture screenshot on failure
      if (result.getStatus() == ITestResult.FAILURE) {
        LOGGER.error("❌ Test failed - capturing screenshot...");
        AllureHelper.captureScreenshot(driver, "FAILURE-" + result.getName());
        AllureHelper.attachPageSource(driver);
        AllureHelper.logBrowserInfo(driver);
      } else if (result.getStatus() == ITestResult.SUCCESS) {
        LOGGER.info("✅ Test passed - capturing success screenshot...");
        AllureHelper.captureScreenshot(driver, "SUCCESS-" + result.getName());
      }

      LOGGER.info("Closing browser...");
      driver.quit();
      LOGGER.info("Browser closed successfully");
      LOGGER.info("========================================\n");
    }
  }
}
