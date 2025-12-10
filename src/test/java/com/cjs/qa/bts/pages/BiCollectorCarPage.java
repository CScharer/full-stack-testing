package com.cjs.qa.bts.pages;

import com.cjs.qa.core.Environment;
import com.cjs.qa.selenium.ISelenium;
import com.cjs.qa.selenium.Page;
import io.cucumber.datatable.DataTable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BiCollectorCarPage extends Page {
  public BiCollectorCarPage(WebDriver webDriver) {
    super(webDriver);
  }

  // DECLARATIONS
  private static final By checkboxPolicyManuallyEntered = By.id("SRC_BUS");
  private static final By editReferral = By.id("REFRL");
  private static final By dropdownException = By.id("EXCPTN");
  private static final String pageTitle = "BI_CollectorCarPage";

  private String getPageTitle() {
    return pageTitle;
  }

  public void verifyPage() {
    verifyTitle(getPageTitle());
  }

  // METHODS GET
  public String getCheckboxPolicyManuallyEntered() {
    return getCheckbox(checkboxPolicyManuallyEntered);
  }

  public String getEditReferral() {
    return getEdit(editReferral);
  }

  public String getDropdownException() {
    return getDropdown(dropdownException);
  }

  // METHODS SET
  public void toggleCheckboxPolicyManuallyEntered() {
    toggleCheckbox(checkboxPolicyManuallyEntered);
  }

  public void setCheckboxPolicyManuallyEntered(String value) {
    setCheckbox(checkboxPolicyManuallyEntered, value);
  }

  public void setEditReferral(String value) {
    setEdit(editReferral, value);
  }

  public void selectDropdownException(String value) {
    selectDropdown(dropdownException, value);
  }

  // SWITCHES POPULATE
  public void populatePage(DataTable table) {
    final List<List<String>> data = table.asLists();
    for (final List<?> item : data) {
      final String field = (String) item.get(0);
      final String value = (String) item.get(1);
      if (!value.isEmpty()) {
        switch (field.toLowerCase(Locale.ENGLISH)) {
          case "policy manually Entered":
            setCheckboxPolicyManuallyEntered(value);
            break;
          case "referral":
            setEditReferral(value);
            break;
          case "exception":
            selectDropdownException(value);
            break;
          default:
            Environment.sysOut("[" + field + "]" + ISelenium.FIELD_NOT_CODED);
            break;
        }
      }
    }
  }

  // SWITCHES VALIDATE
  public void validatePage(DataTable table) {
    final Map<String, String> expected = new HashMap<>();
    final Map<String, String> actual = new HashMap<>();
    final List<List<String>> data = table.asLists();
    for (final List<?> item : data) {
      final String field = (String) item.get(0);
      String value = (String) item.get(1);
      if (!value.isEmpty()) {
        if (Environment.isLogAll()) {
          Environment.sysOut("({Field}" + field + ", {Value}" + value + ");");
        }
        expected.put(field, value);
        switch (field.toLowerCase(Locale.ENGLISH)) {
          case "policy manually Entered":
            value = getCheckboxPolicyManuallyEntered();
            break;
          case "referral":
            value = getEditReferral();
            break;
          case "exception":
            value = getDropdownException();
            break;
          default:
            value = "[" + field + "]" + ISelenium.FIELD_NOT_CODED;
            Environment.sysOut(value);
            break;
        }
        actual.put(field, value);
      }
    }
    Assert.assertSame(getPageTitle() + " validatePage", expected, actual);
  }
}
