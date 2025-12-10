package com.cjs.qa.utilities;

import com.cjs.qa.core.Environment;
import io.cucumber.java.Scenario;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Assert;

// Implementation Of Soft Assertion
public class SoftAssert {
  public static final String HEADER_ACTUAL =
      "***ACTUAL DOES NOT MATCH EXPECTED***"
          + Constants.NEWLINE
          + "Actual:"
          + Constants.NEWLINE
          + "[";
  public static final String COMPARE_DIVIDER = Constants.NL;
  public static final String HEADER_EXPECTED = "Expected:" + Constants.NEWLINE + "[";
  public static final String TRAILER = "]";
  // private final List<String> listAssertionFailures = new ArrayList<>();
  private Map<String, String> failureMap = new HashMap<>();

  public void assertAll() {
    assertAll(null);
  }

  public void assertAll(Scenario scenario) {
    if (!failureMap.isEmpty()) {
      for (final Entry<String, String> entry : failureMap.entrySet()) {
        String key = entry.getKey();
        // String value = (String) entry.getValue();
        key = key.trim();
        if (scenario == null) {
          Environment.sysOut(key + ":" + Constants.NL + failureMap.get(key));
        } else {
          scenario.log(key + ":" + Constants.NL + failureMap.get(key));
        }
      }
      if (scenario == null) {
        Assert.fail();
      } else {
        Assert.fail(
            scenario.getId()
                + Constants.NEWLINE
                + scenario.getName()
                + Constants.NEWLINE
                + scenario.getSourceTagNames());
      }
      failureMap = new HashMap<>();
    }
  }

  public boolean assertEquals(
      Map<String, String> valueExpected, Map<String, String> valueActual, String key) {
    if (!valueActual.equals(valueExpected)) {
      failureMap.put(
          key,
          HEADER_ACTUAL
              + valueActual.toString()
              + TRAILER
              + COMPARE_DIVIDER
              + HEADER_EXPECTED
              + valueExpected.toString()
              + TRAILER);
      return false;
    }
    return true;
  }

  public boolean assertEquals(List<String> valueExpected, List<String> valueActual, String key) {
    if (!valueActual.equals(valueExpected)) {
      // failureMap.put(key, HEADER_ACTUAL + valueActual.toString() +
      // TRAILER + COMPARE_DIVIDER + HEADER_EXPECTED +
      // valueExpected.toString() + TRAILER);
      final StringBuilder stringBuilderAdded = new StringBuilder();
      for (final String value : valueActual) {
        if (!valueExpected.contains(value)) {
          stringBuilderAdded.append(value + Constants.NEWLINE);
        }
      }
      final StringBuilder stringBuilderDeleted = new StringBuilder();
      for (final String value : valueExpected) {
        if (!valueActual.contains(value)) {
          stringBuilderDeleted.append(value + Constants.NEWLINE);
        }
      }
      if (!stringBuilderAdded.toString().isEmpty()) {
        // failureMap.put(key + "[Added]",
        // stringBuilderAdded.toString());
        failureMap.put(
            key + "[Exists in Actual; but Missing from Expected]", stringBuilderAdded.toString());
      }
      if (!stringBuilderDeleted.toString().isEmpty()) {
        // failureMap.put(key + "[Deleted]",
        // stringBuilderDeleted.toString());
        failureMap.put(
            key + "[Exists in Expected; but Missing from Actual]", stringBuilderDeleted.toString());
      }
      return false;
    }
    return true;
  }

  public boolean assertEquals(String valueExpected, String valueActual, String key) {
    if (!valueActual.equals(valueExpected)) {
      failureMap.put(
          key,
          HEADER_ACTUAL
              + valueActual.toString()
              + TRAILER
              + COMPARE_DIVIDER
              + HEADER_EXPECTED
              + valueExpected.toString()
              + TRAILER);
      return false;
    }
    return true;
  }

  public boolean assertEquals(String[] valueExpected, String[] valueActual, String key) {
    if (!valueActual.equals(valueExpected)) {
      failureMap.put(
          key,
          HEADER_ACTUAL
              + valueActual.toString()
              + TRAILER
              + COMPARE_DIVIDER
              + HEADER_EXPECTED
              + valueExpected.toString()
              + TRAILER);
      return false;
    }
    return true;
  }
}
