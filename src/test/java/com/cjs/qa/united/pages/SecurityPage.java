package com.cjs.qa.united.pages;

import com.cjs.qa.core.Environment;
import com.cjs.qa.core.QAException;
import com.cjs.qa.core.security.EPasswords;
import com.cjs.qa.selenium.Page;
import com.cjs.qa.utilities.Constants;
import com.cjs.qa.utilities.JavaHelpers;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class SecurityPage extends Page {
  public SecurityPage(WebDriver webDriver) {
    super(webDriver);
  }

  private static final By optionDontRememberThisDevice =
      By.xpath(".//*[@id='authQuestionsForm']/div/div/label[@for='IsRememberDevice_False']");
  private static final By buttonNext = By.xpath(".//*[@id='btnNext']");

  public void buttonNextClick() throws QAException {
    clickObject(buttonNext);
  }

  @Override
  protected void selectDropdownWithPartialText(By by, String value) {
    if (Environment.isLogAll()) {
      Environment.sysOut("({Field}" + by.toString() + ", {Value}" + value + ");");
    }
    final WebElement webElement = getWebDriver().findElement(by);
    final Select dropdown = new Select(webElement);
    final List<WebElement> list = webElement.findElements(By.xpath("./option"));
    final Iterator<WebElement> iterator = list.iterator();
    while (iterator.hasNext()) {
      final WebElement webElementIterator = iterator.next();
      if (webElementIterator.getText().contains(value)) {
        dropdown.selectByValue(webElementIterator.getAttribute("value"));
        break;
      }
    }
  }

  public void answerSecurityQuestions() throws QAException {
    while (!getWebDriver().getCurrentUrl().contains("security")) {
      sleep(1);
    }
    JavaHelpers.sleep(1);
    Map<String, String> answersMap = new HashMap<>();
    final List<String> questionsList =
        Arrays.asList(
            EPasswords.UNITED_SECURITY_QUESTIONS.getValue().split(Constants.DELIMETER_LIST));
    final List<String> answersList =
        Arrays.asList(
            EPasswords.UNITED_SECURITY_ANSWERS.getValue().split(Constants.DELIMETER_LIST));
    for (int questionsIndex = 0; questionsIndex < questionsList.size(); questionsIndex++) {
      answersMap.put(questionsList.get(questionsIndex), answersList.get(questionsIndex));
    }
    Environment.sysOut("answersMap:[" + answersMap.toString() + "]");
    List<WebElement> questionsWebElementList =
        getWebDriver().findElements(By.xpath(".//*[@id='authQuestionsForm']/div/fieldset/legend"));
    for (int indexWebElement = 0;
        indexWebElement < questionsWebElementList.size();
        indexWebElement++) {
      WebElement webElement = questionsWebElementList.get(indexWebElement);
      String question = webElement.getText();
      String answer = answersMap.get(question);
      Environment.sysOut("webElement Question:[" + question + "], Answer:[" + answer + "]");
      By byAnswer = By.xpath(".//select[@id='QuestionsList_" + indexWebElement + "__AnswerKey']");
      // selectDropdown(byAnswer, answer);
      selectDropdownWithPartialText(byAnswer, answer);
      JavaHelpers.sleep(1);
      // byAnswer=By.xpath(".//*[@id='QuestionsList_0__AnswerKey']/option");
      // selectOption(byAnswer, answer);
    }
    clickObject(optionDontRememberThisDevice);
    buttonNextClick();
  }
}
