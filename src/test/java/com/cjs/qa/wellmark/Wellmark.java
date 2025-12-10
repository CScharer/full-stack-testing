package com.cjs.qa.wellmark;

import com.cjs.qa.wellmark.pages.ClaimsAndSpendingPage;
import com.cjs.qa.wellmark.pages.DetailsPage;
import com.cjs.qa.wellmark.pages.HomePage;
import com.cjs.qa.wellmark.pages.LogInPage;
import com.cjs.qa.wellmark.pages.PopUpPage;
import com.cjs.qa.wellmark.pages.TempPage;
import java.time.Duration;
import org.openqa.selenium.WebDriver;

public class Wellmark {
  private ClaimsAndSpendingPage claimsAndSpendingPage;
  private DetailsPage detailsPage;
  private HomePage homePage;
  private LogInPage logInPage;
  private PopUpPage popUpPage;
  private TempPage tempPage;

  public Wellmark(WebDriver webDriver) {
    claimsAndSpendingPage = new ClaimsAndSpendingPage(webDriver);
    detailsPage = new DetailsPage(webDriver);
    homePage = new HomePage(webDriver);
    logInPage = new LogInPage(webDriver);
    popUpPage = new PopUpPage(webDriver);
    tempPage = new TempPage(webDriver);
    webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(120));
  }

  public ClaimsAndSpendingPage getClaimsAndSpendingPage() {
    return claimsAndSpendingPage;
  }

  public DetailsPage getDetailsPage() {
    return detailsPage;
  }

  public HomePage getHomePage() {
    return homePage;
  }

  public LogInPage getLogInPage() {
    return logInPage;
  }

  public PopUpPage getPopUpPage() {
    return popUpPage;
  }

  public TempPage getTempPage() {
    return tempPage;
  }
}
