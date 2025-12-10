package com.cjs.qa.vivit.pages;

import com.cjs.qa.core.Environment;
import com.cjs.qa.selenium.Page;
import com.cjs.qa.utilities.JavaHelpers;
import com.cjs.qa.vivit.VivitDataTests;
import com.cjs.qa.vivit.objects.Forums;
import com.cjs.qa.vivit.objects.Groups;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ForumsPage extends Page {
  public ForumsPage(WebDriver webDriver) {
    super(webDriver);
  }

  private static final By byTableForums =
      By.xpath(".//*[@id='SpContent_Container']/table/tbody/tr[@class!='header']");

  public void getForumData() throws Throwable {
    Environment.sysOut(
        "***ClassMethodDebug***:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    String statusName = "getForumData";
    if (VivitDataTests.successFileExists(statusName)) {
      return;
    }
    // final JDBC jdbc = new JDBC("", "QAAuto");
    // final String sql = JDBCConstants.SELECT_ALL_FROM +
    // "[v_Vivit_GroupPage_Forum_URLs];";
    // final List<Map<String, String>> mapForums =
    // jdbc.queryResultsString(sql, false);
    // jdbc.close();
    // for (final Map<String, String> mapForum : mapForums)
    // {
    // getForums(mapForum.get("ForumsURL"), mapForum.get("Name"));
    // }
    for (final Groups groups : Groups.getGroupsList()) {
      if (JavaHelpers.hasValue(groups.getForumsURL())) {
        Environment.sysOut(
            "Retrieving Data:["
                + JavaHelpers.getCurrentClassMethodName()
                + "], ["
                + groups.getGroupName()
                + "], ["
                + groups.getForumsURL()
                + "]");
        getForums(groups.getForumsURL(), groups.getGroupName());
      } else {
        Environment.sysOut(
            "Retrieving Null:["
                + JavaHelpers.getCurrentClassMethodName()
                + "], ["
                + groups.getGroupName()
                + "], ["
                + groups.getForumsURL()
                + "]");
      }
    }
    List<StringBuilder> sqlStringBuilderList = new ArrayList<>();
    sqlStringBuilderList.add(new StringBuilder("Forums"));
    sqlStringBuilderList.add(Forums.appendRecords());
    VivitDataTests.updateTableFromCurrentToPreviousAndInsert(sqlStringBuilderList);
    VivitDataTests.successFileCreate(statusName);
  }

  private void getForums(String url, String forumPage) {
    getWebDriver().get(url);
    final List<WebElement> listForumRecords = getWebDriver().findElements(byTableForums);
    for (WebElement elementRecord : listForumRecords) {
      String xPath = "./td";
      final List<WebElement> listWebElements = elementRecord.findElements(By.xpath(xPath));
      String groupName = null;
      String forumID = null;
      String name = null;
      String topics = null;
      String posts = null;
      String lastActivity = null;
      String forumURL = null;
      for (int fieldIndex = 0; fieldIndex < listWebElements.size(); fieldIndex++) {
        final WebElement webElement = listWebElements.get(fieldIndex);
        highlightCurrentElement(webElement);
        final String fieldValue = webElement.getText();
        // final String fieldName = mapFields.get(fieldIndex);
        String href = "";
        String forumId = "";
        if (fieldIndex == 0) {
          xPath = "./b/a";
          final WebElement hrefElement = webElement.findElement(By.xpath(xPath));
          href = hrefElement.getAttribute("href");
          forumId = href.split("/")[4];
          // fieldName = mapFields.get(fieldIndex);
        }
        switch (fieldIndex) {
          case 1: // "Topics":
            topics = fieldValue;
            break;
          case 2: // "Posts":
            posts = fieldValue;
            break;
          case 3: // "Latest Activity":
            lastActivity = fieldValue;
            break;
          case 0: // default:
            forumID = forumId;
            groupName = forumPage;
            forumURL = href;
            name = fieldValue;
            break;
          default:
            Environment.sysOut("Unexpected field index: " + fieldIndex + ". Skipping.");
            break;
        }
      }
      Forums.getForumsList()
          .add(
              new Forums(
                  groupName,
                  forumID,
                  name,
                  Integer.valueOf(topics),
                  Integer.valueOf(posts),
                  lastActivity,
                  forumURL));
    }
  }
}
