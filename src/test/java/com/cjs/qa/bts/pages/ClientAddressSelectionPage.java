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

public class ClientAddressSelectionPage extends Page {
  public ClientAddressSelectionPage(WebDriver webDriver) {
    super(webDriver);
  }

  // DECLARATIONS
  private static final String nodeClientSelection = "clientSelectionForm:";
  private static final By buttonCreateNewNameSeq = By.id(nodeClientSelection + "createNewNameBtn");
  private static final By buttonOk = By.id(nodeClientSelection + "buttonOk");
  private static final By buttonCancel = By.id(nodeClientSelection + "cancelBtn");
  private static final String pageTitle = "ClientAddressSelectionPage";

  private String getPageTitle() {
    return pageTitle;
  }

  public void verifyPage() {
    verifyTitle(getPageTitle());
  }

  // METHODS GET
  // METHODS SET
  public void clickButtonCreateNewNameSeq() {
    clickObject(buttonCreateNewNameSeq);
  }

  public void clickButtonOk() {
    clickObject(buttonOk);
  }

  public void clickButtonCancel() {
    clickObject(buttonCancel);
  }

  // SWITCHES POPULATE
  public void populatePage(DataTable table) {
    final List<List<String>> data = table.asLists();
    for (final List<?> item : data) {
      final String field = (String) item.get(0);
      final String value = (String) item.get(1);
      if (!value.isEmpty()) {
        if (Environment.isLogAll()) {
          Environment.sysOut("({Field}" + field + ", {Value}" + value + ");");
        }
        switch (field.toLowerCase(Locale.ENGLISH)) {
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
        expected.put(field, value);
        switch (field.toLowerCase(Locale.ENGLISH)) {
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
