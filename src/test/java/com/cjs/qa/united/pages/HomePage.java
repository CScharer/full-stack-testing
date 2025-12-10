package com.cjs.qa.united.pages;

import com.cjs.qa.core.Environment;
import com.cjs.qa.core.QAException;
import com.cjs.qa.selenium.Page;
import com.cjs.qa.united.UnitedEnvironment;
import com.cjs.qa.utilities.JavaHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends Page {
  public HomePage(WebDriver webDriver) {
    super(webDriver);
  }

  private static final By buttonSignIn = By.xpath(".//*[@id='loginButton']");
  private static final By buttonSignOut = By.xpath("//button/span[.='Sign out']");

  public void buttonSignInClick() throws QAException {
    clickObject(buttonSignIn);
  }

  public void buttonSignOutClick() throws QAException {
    clickObject(buttonSignOut);
    JavaHelpers.sleep(1);
  }

  public void load() {
    maximizeWindow();
    Environment.sysOut("Loading:[" + UnitedEnvironment.URL_LOGIN + "]");
    getWebDriver().get(UnitedEnvironment.URL_LOGIN);
  }
}
