package com.cjs.qa.dropbox;

import com.cjs.qa.dropbox.pages.SignInPage;
import org.openqa.selenium.WebDriver;

public class Dropbox {
  private SignInPage signInPage;

  public SignInPage getSignInPage() {
    return signInPage;
  }

  public Dropbox(WebDriver webDriver) {
    signInPage = new SignInPage(webDriver);
    webDriver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(120));
  }
}
