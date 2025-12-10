package com.cjs.qa.google.pages;

import com.cjs.qa.core.Environment;
import com.cjs.qa.core.QAException;
import com.cjs.qa.google.GoogleEnvironment;
import com.cjs.qa.selenium.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SignInPage extends Page {
  public SignInPage(WebDriver webDriver) {
    super(webDriver);
  }

  private static final By editSearch = By.name("q");

  public void editSearchSet(String value) throws QAException {
    if (objectExists(editSearch)) {
      setEdit(editSearch, value);
    }
  }

  public void load() throws QAException {
    maximizeWindow();
    Environment.sysOut("Loading:[" + GoogleEnvironment.URL_LOGIN + "]");
    getWebDriver().get(GoogleEnvironment.URL_LOGIN);
  }
}
