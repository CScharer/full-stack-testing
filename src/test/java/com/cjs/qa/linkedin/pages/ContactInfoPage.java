package com.cjs.qa.linkedin.pages;

import com.cjs.qa.core.Environment;
import com.cjs.qa.core.QAException;
import com.cjs.qa.core.security.EPasswords;
import com.cjs.qa.linkedin.LinkedIn;
import com.cjs.qa.linkedin.LinkedInEnvironment;
import com.cjs.qa.linkedin.data.DataTests;
import com.cjs.qa.selenium.Page;
import com.cjs.qa.utilities.CJSConstants;
import com.cjs.qa.utilities.Constants;
import com.cjs.qa.utilities.DateHelpersTests;
import com.cjs.qa.utilities.Email;
import com.cjs.qa.utilities.FSOTests;
import com.cjs.qa.utilities.JavaHelpers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ContactInfoPage extends Page {
  public ContactInfoPage(WebDriver webDriver) {
    super(webDriver);
  }

  private String linkedInURL = null;
  private WebElement webElementSection = null;
  private StringBuilder sqlStringBuilder =
      new StringBuilder(); // NOPMD - Intentional mutable field for SQL building across methods
  private List<Map<String, String>> linkedInConnectionContactInfoListMap = new ArrayList<>();
  private Map<String, String> contactInfoMap = new HashMap<>();

  public Map<String, String> getContactInfoMap() {
    return contactInfoMap;
  }

  public void setContactInfoMap(Map<String, String> contactInfoMap) {
    this.contactInfoMap = contactInfoMap;
  }

  public List<Map<String, String>> getLinkedInConnectionContactInfoListMap() {
    return linkedInConnectionContactInfoListMap;
  }

  public void setLinkedInConnectionContactInfoListMap(
      List<Map<String, String>> linkedInConnectionContactInfoListMap) {
    this.linkedInConnectionContactInfoListMap = linkedInConnectionContactInfoListMap;
  }

  public String getLinkedInURL() {
    return linkedInURL;
  }

  public void setLinkedInURL(String linkedInURL) {
    this.linkedInURL = linkedInURL;
  }

  public StringBuilder getStringBuilderSQL() {
    return sqlStringBuilder;
  }

  public void setStringBuilderSQL(StringBuilder sqlStringBuilder) {
    this.sqlStringBuilder = sqlStringBuilder;
  }

  public WebElement getWebElementSection() {
    return webElementSection;
  }

  public void setWebElementSection(WebElement webElementSection) {
    this.webElementSection = webElementSection;
  }

  public List<WebElement> getWebElementSectionList() {
    return getWebDriver()
        .findElements(
            By.xpath(".//div[@class='pv-profile-section__section-info section-info']/section"));
  }

  private void getAddress() {
    Environment.sysOut(
        "***ClassMethodDebug***:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    String methodName = JavaHelpers.getCurrentMethodName();
    try {
      WebElement webElement = getWebElementSection().findElement(By.xpath("./div/a"));
      String address = webElement.getText();
      Environment.sysOut(methodName + ":address:[" + address + "]");
      appendLinkedInField(methodName, address);
    } catch (Exception throwable) {
      throwError(methodName);
    }
  }

  private void getBirthday() {
    Environment.sysOut(
        "***ClassMethodDebug***:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    String methodName = JavaHelpers.getCurrentMethodName();
    try {
      WebElement webElement = getWebElementSection().findElement(By.xpath("./div/span"));
      String birthday = webElement.getText();
      Environment.sysOut(methodName + ":birthday:[" + birthday + "]");
      appendLinkedInField(methodName, birthday);
    } catch (Exception throwable) {
      throwError(methodName);
    }
  }

  private void getConnected() {
    Environment.sysOut(
        "***ClassMethodDebug***:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    String methodName = JavaHelpers.getCurrentMethodName();
    try {

      WebElement webElement = getWebElementSection().findElement(By.xpath("./div/span"));
      String connected = webElement.getText();
      Environment.sysOut(methodName + ":connected:[" + connected + "]");
    } catch (Exception throwable) {
      throwError(methodName);
    }
  }

  private void getEmail() {
    Environment.sysOut(
        "***ClassMethodDebug***:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    String methodName = JavaHelpers.getCurrentMethodName();
    try {

      WebElement webElement = getWebElementSection().findElement(By.xpath("./div/a"));
      String eMail = webElement.getText();
      Environment.sysOut(methodName + ":eMail:[" + eMail + "]");
      appendLinkedInField(methodName + " (Work)", eMail);
    } catch (Exception throwable) {
      throwError(methodName);
    }
  }

  private void getIM() {
    Environment.sysOut(
        "***ClassMethodDebug***:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    String methodName = JavaHelpers.getCurrentMethodName();
    try {
      WebElement webElement = getWebElementSection().findElement(By.xpath("./ul/li/span"));
      String im = webElement.getText();
      Environment.sysOut(methodName + ":im:[" + im + "]");
      appendLinkedInField(methodName, im);
    } catch (Exception throwable) {
      throwError(methodName);
    }
  }

  private void getPhone() {
    Environment.sysOut(
        "***ClassMethodDebug***:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    String methodName = JavaHelpers.getCurrentMethodName();
    try {

      List<WebElement> webElementPhoneList = null;
      webElementPhoneList = getWebElementSection().findElements(By.xpath("./ul/li/span"));
      int index = 0;
      String number = "";
      String type = "";
      for (WebElement webElement : webElementPhoneList) {
        index++;
        if (index % 2 == 0) {
          type = webElement.getText();
          String phone = "value:[" + number + "], type:[" + type + "]";
          Environment.sysOut(methodName + ":phone:{" + phone + "}");
          switch (type) {
              // case "(Direct)":
            case "(Home)":
            case "(Mobile)":
              // case "(Personal)":
            case "(Work)":
            default:
              appendLinkedInField(methodName + " " + type, number);
              break;
          }
        } else {
          number = webElement.getText();
        }
      }
    } catch (Exception throwable) {
      throwError(methodName);
    }
  }

  private void getTwitter() {
    Environment.sysOut(
        "***ClassMethodDebug***:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    String methodName = JavaHelpers.getCurrentMethodName();
    try {

      List<WebElement> webElementList = null;
      webElementList = getWebElementSection().findElements(By.xpath("./ul"));
      for (WebElement webElementTwitter : webElementList) {
        WebElement webElement = webElementTwitter.findElement(By.xpath("./li/a"));
        String twitter = webElement.getAttribute("href");
        Environment.sysOut(methodName + ":twitter:[" + twitter + "]");
        appendLinkedInField(methodName, twitter);
      }
    } catch (Exception throwable) {
      throwError(methodName);
    }
  }

  private void getWebsite() {
    Environment.sysOut(
        "***ClassMethodDebug***:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    String methodName = JavaHelpers.getCurrentMethodName();
    try {

      List<WebElement> webElementList = getWebElementSection().findElements(By.xpath("./ul/li"));
      for (WebElement webElementSite : webElementList) {
        WebElement webElement = webElementSite.findElement(By.xpath("./div/span"));
        String type = webElement.getText();
        webElement = webElementSite.findElement(By.xpath("./div/a"));
        String site = webElement.getText();
        String website = "type:[" + type + "], site:[" + site + "]";
        Environment.sysOut(methodName + ":website:{" + website + "}");
        switch (type) {
          case "(Blog)":
          case "(Company)":
            appendLinkedInField(methodName + " " + type, site);
            break;
          case "(Company Website)":
            appendLinkedInField(methodName + " " + "(Company)", site);
            break;
          default:
            break;
        }
      }
    } catch (Exception throwable) {
      throwError(methodName);
    }
  }

  private void getWebsites() {
    Environment.sysOut(
        "***ClassMethodDebug***:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    String methodName = "getWebsite"; // JavaHelpers.getCurrentMethodName();
    try {
      List<WebElement> webElementList = getWebElementSection().findElements(By.xpath("./ul/li"));
      for (WebElement webElementWebsite : webElementList) {
        WebElement webElement = webElementWebsite.findElement(By.xpath("./div/a"));
        String site = webElement.getAttribute("href");
        webElement = webElementWebsite.findElement(By.xpath("./div/span"));
        String type = webElement.getText();
        String website = "type:[" + type + "], site:[" + site + "]";
        Environment.sysOut(methodName + ":website:{" + website + "}");
        switch (type) {
          case "(Blog)":
          case "(Company)":
            appendLinkedInField(methodName + " " + type, site);
            break;
          case "(Company Website)":
            appendLinkedInField(methodName + " " + "(Company)", site);
            break;
          default:
            break;
        }
      }
    } catch (Exception throwable) {
      throwError(methodName + "s");
    }
  }

  public void getContactInfoPageData(List<Map<String, String>> linkedInMapList) {
    Environment.sysOut(
        "***ClassMethodDebug***:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    final String buffer = JavaHelpers.createBufferString("*", 20, null);
    List<String> sectionList = new ArrayList<>();
    for (int mapIndex = 0; mapIndex < linkedInMapList.size(); mapIndex++) {
      Environment.sysOut(buffer);
      Environment.sysOut("Processing Record:" + (mapIndex + 1) + " of " + linkedInMapList.size());
      Map<String, String> map = linkedInMapList.get(mapIndex);
      if (JavaHelpers.hasValue(map.get("First Name"))
          && JavaHelpers.hasValue(map.get("Last Name"))
          && JavaHelpers.hasValue(map.get(DataTests.FIELD_LINKEDIN_URL))) {
        String linkedInURL = map.get(DataTests.FIELD_LINKEDIN_URL);
        String linkedInURLDetailContactInfo =
            LinkedIn.LINKEDIN_URL + linkedInURL + "/detail/contact-info";
        getWebDriver().get(linkedInURLDetailContactInfo);
        LinkedIn.sleepRandom(2, 4, 250, 750);
        getData(sectionList, linkedInURL);
      }
    }
    Environment.sysOut(buffer);
    sectionList.sort(null);
    Environment.sysOut("sectionList:[" + sectionList + "]");
    updateLinkedInTable();
  }

  public void getData(List<String> sectionList, String linkedInURL) {
    Environment.sysOut(
        "***ClassMethodDebug***:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    String methodName = JavaHelpers.getCurrentMethodName();
    Environment.sysOut(methodName + ":linkedInURL:[" + linkedInURL + "]");
    setLinkedInURL(linkedInURL);
    List<WebElement> webElementSectionList = getWebElementSectionList();
    setContactInfoMap(new HashMap<>());
    for (WebElement webElementSection : webElementSectionList) {
      setWebElementSection(webElementSection);
      WebElement webElementHeader = getWebElementSection().findElement(By.xpath("./header"));
      String header = webElementHeader.getText();
      if (!header.contains(" Profile")) {
        if (!sectionList.contains(header)) {
          sectionList.add(header);
        }
        getValues(header);
      }
    }
    String lastUpdated =
        DateHelpersTests.getCurrentDateTime(DateHelpersTests.FORMAT_US_STANDARD_DATE);
    if (getContactInfoMap() != null) {
      getContactInfoMap().put(DataTests.FIELD_LINKEDIN_URL, linkedInURL);
      appendLinkedInField("getLast Updated", lastUpdated);
      getLinkedInConnectionContactInfoListMap().add(getContactInfoMap());
    }
  }

  public boolean getValues(String header) {
    Environment.sysOut(
        "***ClassMethodDebug***:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    String methodName = JavaHelpers.getCurrentMethodName();
    try {
      switch (header) {
        case "Address":
          getAddress();
          break;
        case "Birthday":
          getBirthday();
          break;
        case "Connected":
          getConnected();
          break;
        case "Email":
          getEmail();
          break;
        case "IM":
          getIM();
          break;
        case "Phone":
          getPhone();
          break;
        case "Twitter":
          getTwitter();
          break;
        case "Website":
          getWebsite();
          break;
        case "Websites":
          getWebsites();
          break;
        default:
          break;
      }
    } catch (Exception throwable) {
      throwError(methodName + ":" + header);
      return false;
    }
    return true;
  }

  private void throwError(String methodName) {
    Environment.sysOut(methodName + ":ERROR");
  }

  public void appendLinkedInField(String fieldName, String fieldValue) {
    Environment.sysOut(
        "***ClassMethodDebug***:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    String methodName = JavaHelpers.getCurrentMethodName();
    try {
      fieldName = fieldName.substring(3);
      String fieldNameMap = getContactInfoMap().get(fieldName);
      if (fieldNameMap == null) {
        getContactInfoMap().put(fieldName, fieldValue);
      } else {
        getContactInfoMap().put(fieldName, fieldNameMap + "\n" + fieldValue);
      }
    } catch (Exception throwable) {
      throwError(methodName + ":fieldName:[" + fieldName + "], fieldValue:[" + fieldValue + "]");
    }
  }

  public void appendLinkedInRecord(Map<String, String> map) {
    Environment.sysOut(
        "***ClassMethodDebug***:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    String methodName = JavaHelpers.getCurrentMethodName();
    try {
      StringBuilder stringBuilderData = new StringBuilder();
      for (Entry<String, String> entry : map.entrySet()) {
        String fieldName = entry.getKey();
        String fieldValue = entry.getValue();
        if (!DataTests.FIELD_LINKEDIN_URL.equals(fieldName)) {
          if (stringBuilderData.length() > 0) {
            stringBuilderData.append(",");
          }
          stringBuilderData.append(
              "[" + fieldName + "]='" + fieldValue.replaceAll("'", "''") + "'");
        }
      }
      if (!stringBuilderData.toString().isEmpty()) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE [" + DataTests.TABLE_LINKEDIN + "] ");
        stringBuilder.append("SET ");
        stringBuilder.append(stringBuilderData.toString());
        stringBuilder.append(
            " WHERE ["
                + DataTests.FIELD_LINKEDIN_URL
                + "]='"
                + map.get(DataTests.FIELD_LINKEDIN_URL)
                + "';");
        Environment.sysOut(methodName + ":Adding record[" + stringBuilder.toString() + "]");
        setStringBuilderSQL(getStringBuilderSQL().append(stringBuilder.toString()));
      }
    } catch (Exception throwable) {
      throwError(methodName + ":" + "");
    }
  }

  public void updateLinkedInTable() {
    Environment.sysOut(
        "***ClassMethodDebug***:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    String methodName = JavaHelpers.getCurrentMethodName();
    String message = "";
    try {
      setStringBuilderSQL(new StringBuilder());
      for (int mapIndex = 0;
          mapIndex < getLinkedInConnectionContactInfoListMap().size();
          mapIndex++) {
        Map<String, String> contactInfoMap =
            getLinkedInConnectionContactInfoListMap().get(mapIndex);
        appendLinkedInRecord(contactInfoMap);
      }
      if (getStringBuilderSQL().length() > 0) {
        FSOTests.fileWrite(
            LinkedInEnvironment.FILE_LOG,
            sqlStringBuilder.toString().replaceAll(";", Constants.NEWLINE + ';'),
            false);
        int recordsAffected = DataTests.getJdbc().executeUpdate(sqlStringBuilder.toString(), false);
        message =
            Constants.NEWLINE + sqlStringBuilder.toString().replaceAll(";", Constants.NEWLINE);
        Environment.sysOut("recordsAffected:[" + recordsAffected + "]");
        Email.sendEmail(
            CJSConstants.EMAIL_ADDRESS_GMAIL,
            EPasswords.EMAIL_GMAIL.getValue(),
            CJSConstants.EMAIL_ADDRESS_GMAIL,
            "",
            "",
            "LinkedIn Records Affected (" + recordsAffected + ")",
            message,
            "");
      }
    } catch (Exception | QAException throwable) {
      throwError(methodName + ":" + message);
    }
  }
}
