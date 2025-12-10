package com.cjs.qa.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

public class SeleniumWebDriverEventListener implements WebDriverListener {

  public void beforeAlertAccept(WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName());
  }

  public void afterAlertAccept(WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName());
  }

  public void afterAlertDismiss(WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName());
  }

  public void beforeAlertDismiss(WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName());
  }

  public void beforeNavigateTo(String url, WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName() + "-url:[" +
    // url + "]");
  }

  public void afterNavigateTo(String url, WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName() + "-url:[" +
    // url + "]");
  }

  public void beforeNavigateBack(WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName());
  }

  public void afterNavigateBack(WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName());
  }

  public void beforeNavigateForward(WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName());
  }

  public void afterNavigateForward(WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName());
  }

  public void beforeNavigateRefresh(WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName());
  }

  public void afterNavigateRefresh(WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName());
  }

  public void beforeFindBy(By by, WebElement webElement, WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName() + "-by:[" +
    // by.toString() + "], webElement:[" + webElement.toString() + "]");
  }

  public void afterFindBy(By by, WebElement webElement, WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName() + "-by:[" +
    // by.toString() + "], webElement:[" + webElement.toString() + "]");
  }

  public void beforeClickOn(WebElement webElement, WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName() +
    // "-webElement:[" + webElement.toString() + "]");
  }

  public void afterClickOn(WebElement webElement, WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName() +
    // "-webElement:[" + webElement.toString() + "]");
  }

  public void beforeChangeValueOf(
      WebElement webElement, WebDriver webDriver, CharSequence[] keysToSend) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName() +
    // "-webElement:[" + webElement.toString() + "], keysToSend:[" +
    // keysToSend[0].toString() + "]");
  }

  public void afterChangeValueOf(
      WebElement webElement, WebDriver webDriver, CharSequence[] keysToSend) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName() +
    // "-webElement:[" + webElement.toString() + "], keysToSend:[" +
    // keysToSend[0].toString() + "]");
  }

  public void beforeScript(String script, WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName() + "-script:[" +
    // script + "]");
  }

  public void afterScript(String script, WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName() + "-script:[" +
    // script + "]");
  }

  public void onException(Throwable throwable, WebDriver webDriver) {
    // Environment.sysOut(JavaHelpers.getCurrentMethodName());
  }
}
