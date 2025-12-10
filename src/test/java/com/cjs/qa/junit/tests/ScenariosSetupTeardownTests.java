package com.cjs.qa.junit.tests;

import com.cjs.qa.core.Environment;
import com.cjs.qa.selenium.Selenium;
import com.cjs.qa.utilities.JavaHelpers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScenariosSetupTeardownTests {
  // private static RTestRun rTestRun = new RTestRun("CJS", "Starting");
  // private static RTestReporter rTestReporter = new RTestReporter();
  @Rule public TestName testName = new TestName();

  @Rule
  public TestWatcher testWatcher =
      new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
          // rTestRun.cRTestSet().cRScenario().cRTest().addStep(description,
          // "Failed");
          Environment.sysOut(
              description.getClassName()
                  + "|"
                  + description.getMethodName()
                  + "|"
                  + description.getDisplayName());
        }

        @Override
        protected void finished(Description description) {
          // rTestRun.cRTestSet().cRScenario().cRTest().addStep(description,
          // "Finished");
          Environment.sysOut(
              description.getClassName()
                  + "|"
                  + description.getMethodName()
                  + "|"
                  + description.getDisplayName());
        }

        @Override
        protected void skipped(AssumptionViolatedException e, Description description) {
          // rTestRun.cRTestSet().cRScenario().cRTest().addStep(description,
          // "Skipped");
          Environment.sysOut(
              description.getClassName()
                  + "|"
                  + description.getMethodName()
                  + "|"
                  + description.getDisplayName());
        }

        @Override
        protected void starting(Description description) {
          // rTestRun.cRTestSet().cRScenario().cRTest().addStep(description,
          // "Starting");
          Environment.sysOut(
              description.getClassName()
                  + "|"
                  + description.getMethodName()
                  + "|"
                  + description.getDisplayName());
        }

        @Override
        protected void succeeded(Description description) {
          // rTestRun.cRTestSet().cRScenario().cRTest().addStep(description,
          // "Succeeded");
          Environment.sysOut(
              description.getClassName()
                  + "|"
                  + description.getMethodName()
                  + "|"
                  + description.getDisplayName());
        }
      };

  @BeforeClass
  public static void classSetup() {
    Environment.sysOut("Setup-Class Method:[" + JavaHelpers.getCurrentClassName() + "]");
    // rTestRun.addTestSet(JavaHelpers.getClassName(true), "Starting");
    // rTestRun.cRTestSet().addScenario(JavaHelpers.getClassName(true),
    // "Starting");
    // testReporter = new TestReporter();
  }

  @Before
  public void testSetup() {
    Environment.sysOut("Setup-Test Method:[" + getTestName() + "]");
    // rTestRun.cRTestSet().cRScenario().addTest(getTestName(), "Starting");
    // testReporter.addTest(getTestName());
    // testReporter.reportTest(getTestName());
  }

  @After
  public void testTeardown() {
    Environment.sysOut("TearDown-Test Method:[" + getTestName() + "]");
    // rTestRun.cRTestSet().cRScenario().setScenarioStatus("Finished");
  }

  @AfterClass
  public static void classTearDown() {
    Environment.sysOut("TearDown-Class Method:[" + JavaHelpers.getCurrentClassName() + "]");
    // rTestRun.cRTest().setTestStatus("Finished");
    // rTestRun.cRTestSet().setTestSetStatus("Finished");
    // rTestRun.setTestRunStatus("Finished");
    // testReporter.reportAll();
  }

  @Test
  public void t001() {
    Environment.sysOut("Running Test:[" + getTestName() + "]");
    final Selenium mockSelenium = Mockito.mock(Selenium.class);
    Mockito.verify(mockSelenium).getWebDriver();
    Environment.sysOut("mock:[" + mockSelenium.getWebDriver().toString() + "]");
  }

  @Test
  public void t002() {
    Environment.sysOut("Running Test:[" + getTestName() + "]");
    Assert.fail();
  }

  @Test
  @Ignore
  public void tIngore() {
    Environment.sysOut("Running Test:[" + getTestName() + "]");
  }

  @Test
  public void t003() {
    Environment.sysOut("Running Test:[" + getTestName() + "]");
    throw new AssumptionViolatedException("t004");
  }

  @Test
  public void t004() {
    Environment.sysOut("Running Test:[" + getTestName() + "]");
    Assert.fail();
  }

  @Test
  public void t00N() {
    Environment.sysOut("Running Test:[" + getTestName() + "]");
  }

  public String getTestName() {
    return testName.getMethodName();
  }
}
