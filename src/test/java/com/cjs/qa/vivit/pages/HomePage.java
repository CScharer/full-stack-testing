package com.cjs.qa.vivit.pages;

import com.cjs.qa.core.Environment;
import com.cjs.qa.core.QAException;
import com.cjs.qa.selenium.Page;
import com.cjs.qa.utilities.Constants;
import com.cjs.qa.utilities.JavaHelpers;
import com.cjs.qa.vivit.VivitEnvironment;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage extends Page {
  private static final By byLogo = By.xpath(".//*[@id='logo']/img");
  private static final By checkboxRememberMe = By.xpath(".//*[@id='rememberme']");
  private static final By buttonSignIn =
      By.xpath(".//*[@id='ctl00_PageContent_MainLogin']//input[@value='Sign In']");
  private static final By buttonSignOut = By.xpath(".//*[@id='itoolbar']/a[.='Sign Out']");
  private static final By editEmail = By.xpath(".//*[@id='u']");
  private static final By editPassword = By.xpath(".//*[@id='p']");
  private static final By linkSignIn = By.xpath(".//*[@id='itoolbar']/a[.='Sign In']");

  public HomePage(WebDriver webDriver) {
    super(webDriver);
  }

  public void buttonSignInClick() {
    clickObject(buttonSignIn);
  }

  public void checkboxRememberMeSet(String value) {
    setCheckbox(checkboxRememberMe, value);
  }

  public void clickButtonSignOut() {
    clickObject(buttonSignOut);
  }

  public void clickUpdateProfileLink() {
    clickLink("Update Profile");
  }

  public void editEmailSet(String value) {
    setEdit(editEmail, value);
  }

  public void editPasswordSet(String value) {
    setEditPassword(editPassword, value);
  }

  public List<String> getLanguagesSupported() {
    // May need to click on the dropdown to see the elements.
    By byLanguages =
        By.xpath(
            ".//*[@id=':1.menuBody']/table/tbody/tr/td/a/div//span[2][not(contains(.,'Select"
                + " Language'))]");
    StringBuilder stringBuilder = new StringBuilder("Languages Supported:");
    List<WebElement> webElementList = getWebDriver().findElements(byLanguages);
    List<String> languagesSupportedList = new ArrayList<>();
    for (WebElement webElement : webElementList) {
      if (!stringBuilder.toString().isEmpty()) {
        stringBuilder.append(Constants.NEWLINE);
      }
      languagesSupportedList.add(webElement.getText());
      stringBuilder.append(webElement.getText());
    }
    Environment.sysOut(stringBuilder.toString());
    return languagesSupportedList;
  }

  public int getLanguagesSupportedCount() {
    return getLanguagesSupported().size();
  }

  public String getLogoSource() throws QAException {
    maximizeWindow();
    Environment.sysOut("Loading:[" + VivitEnvironment.URL_LOGIN + "]");
    WebElement webElement = null;
    int attempt = 0;
    final int attemptsMax = 3;
    String logoSource = null;
    do {
      try {
        attempt++;
        Environment.sysOut(JavaHelpers.getCurrentMethodName() + " attempt " + attempt);
        if (attempt > 0) {
          getWebDriver().get(VivitEnvironment.URL_LOGIN);
        }
        webElement = getWebElement(byLogo);
        logoSource = webElement.getAttribute("src");
        return logoSource;
      } catch (final Exception e) {
        Environment.sysOut(
            QAException.ERROR + JavaHelpers.getCurrentClassMethodName() + ":Error Finding Logo.");
        if (attempt > attemptsMax) {
          throw new QAException("***ERROR***:[" + e.getMessage() + "]", e);
        }
      }
    } while (webElement == null);
    return logoSource;
  }

  public void linkSignInClick() {
    clickObject(linkSignIn);
  }

  public void signIn(String eMail, String password, String rememberMe) throws QAException {
    boolean success = false;
    int attempt = 0;
    final int attemptsMax = 3;
    do {
      attempt++;
      try {
        getWebDriver().get(VivitEnvironment.URL_LOGIN);
        JavaHelpers.sleep(2);
        Environment.sysOut(JavaHelpers.getCurrentMethodName() + " attempt " + attempt);
        linkSignInClick();
        editEmailSet(eMail);
        editPasswordSet(password);
        checkboxRememberMeSet(rememberMe);
        buttonSignInClick();
        success = true;
      } catch (Exception e) {
        Environment.sysOut(
            QAException.ERROR + JavaHelpers.getCurrentClassMethodName() + ":Error Signing In.");
        if (attempt > attemptsMax) {
          throw new QAException(QAException.ERROR + "[" + e.getMessage() + "]", e);
        }
      }
    } while (!success);
  }
}
