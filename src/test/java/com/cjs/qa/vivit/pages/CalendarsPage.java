package com.cjs.qa.vivit.pages;

import com.cjs.qa.core.Environment;
import com.cjs.qa.selenium.Page;
import com.cjs.qa.utilities.Constants;
import com.cjs.qa.utilities.JavaHelpers;
import com.cjs.qa.vivit.VivitDataTests;
import com.cjs.qa.vivit.objects.Calendars;
import com.cjs.qa.vivit.objects.Groups;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CalendarsPage extends Page {
  public CalendarsPage(WebDriver webDriver) {
    super(webDriver);
  }

  private String calendarUrlCommunity = "https://www.vivit-worldwide.org/events/event_list.asp";
  private static final By byTableCalendars = By.xpath(".//*[@id='EventList']/tbody/tr/td[1]/div");

  private String getCalendarUrlCommunity() {
    return calendarUrlCommunity;
  }

  public void getCalendarData() throws Throwable {
    Environment.sysOut(
        "***ClassMethodDebug***:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    String statusName = "getCalendarData";
    if (VivitDataTests.successFileExists(statusName)) {
      return;
    }
    for (final Groups groups : Groups.getGroupsList()) {
      if (JavaHelpers.hasValue(groups.getCalendarURL())) {
        Environment.sysOut(
            "Retrieving Data:["
                + JavaHelpers.getCurrentClassMethodName()
                + "], ["
                + groups.getGroupName()
                + "], ["
                + groups.getCalendarURL()
                + "]");
        getCalendars(groups.getCalendarURL(), groups.getGroupName());
      } else {
        Environment.sysOut(
            "Retrieving Null:["
                + JavaHelpers.getCurrentClassMethodName()
                + "], ["
                + groups.getGroupName()
                + "], ["
                + groups.getCalendarURL()
                + "]");
      }
    }
    getCalendars(getCalendarUrlCommunity(), "Community");
    List<StringBuilder> sqlStringBuilderList = new ArrayList<>();
    sqlStringBuilderList.add(new StringBuilder("Calendars"));
    sqlStringBuilderList.add(Calendars.appendRecords());
    VivitDataTests.updateTableFromCurrentToPreviousAndInsert(sqlStringBuilderList);
    VivitDataTests.successFileCreate(statusName);
  }

  private void getCalendars(String url, String calendarPage) {
    getWebDriver().get(url);
    final List<WebElement> listCalendarRecords = getWebDriver().findElements(byTableCalendars);
    for (WebElement elementRecord : listCalendarRecords) {
      String xPath = "./span/a";
      WebElement webElement = elementRecord.findElement(By.xpath(xPath));
      highlightCurrentElement(webElement);
      final String calendarID = webElement.getAttribute("href");
      xPath = "./span/a/b";
      webElement = elementRecord.findElement(By.xpath(xPath));
      highlightCurrentElement(webElement);
      final String eventName = webElement.getText();
      // xPath = "./h3";
      // webElement = elementRecord.findElement(By.xpath(xPath));
      highlightCurrentElement(webElement);
      String eventDate = elementRecord.getText();
      // eventDate = eventDate.substring(0, (eventDate.indexOf("Time:") -
      // 1));
      // eventDate = eventDate.replace(eventName, "");
      final String[] eventData = eventDate.split(Constants.BACKSLASH + "n");
      eventDate = eventData[1];
      xPath = "./div[1]";
      webElement = elementRecord.findElement(By.xpath(xPath));
      highlightCurrentElement(webElement);
      final String eventDetails = webElement.getText();
      Calendars.getCalendarsList()
          .add(
              new Calendars(
                  calendarID, calendarPage, eventName, eventDate, eventDetails, calendarID));
    }
  }
}
