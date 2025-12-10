package com.cjs.qa.polkcounty;

import com.cjs.qa.polkcounty.pages.Details;
import com.cjs.qa.polkcounty.pages.Main;
import org.openqa.selenium.WebDriver;

public class PolkCounty {
  private Main main;
  private Details details;

  public PolkCounty(WebDriver webDriver) {
    main = new Main(webDriver);
    details = new Details(webDriver);
    webDriver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(120));
  }

  public Main getMain() {
    return main;
  }

  public Details getDetails() {
    return details;
  }
}
