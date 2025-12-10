package com.cjs.qa.wellmark.pages;

import com.cjs.qa.core.QAException;
import com.cjs.qa.selenium.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends Page {
  public HomePage(WebDriver webDriver) {
    super(webDriver);
  }

  private static final By linkSeeAllClaims = By.xpath(".//*[@id='linkToSeeAllClaims']");

  public void linkSeeAllClaimsClick() throws QAException {
    waitPageLoaded();
    clickObject(linkSeeAllClaims);
  }
}
