package com.cjs.qa.americanairlines;

import com.cjs.qa.americanairlines.pages.VacationabilityPage;
import org.openqa.selenium.WebDriver;

public class AmericanAirlines {
  private VacationabilityPage vacationabilityPage;

  public VacationabilityPage getVacationabilityPage() {
    return vacationabilityPage;
  }

  public AmericanAirlines(WebDriver webDriver) {
    vacationabilityPage = new VacationabilityPage(webDriver);
    webDriver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(120));
  }
}
