package com.cjs.qa.utilities;

import com.cjs.qa.core.Environment;
import java.util.Locale;

public class ReadFromExcel {

  public void whichTestType(String type) {
    // Java 17: Switch expression (void method, so using block syntax)
    switch (type.toLowerCase(Locale.ENGLISH)) {
      case "policyverification" -> {
        // go to policyVerification class to do all the things
      }
      case "policyverificationbuild" -> {
        // go to policyVerificationBuild class to do all the things
      }
      case "policyentry" -> {
        // go to policyEntry class to do all the things
      }
      case "policyentrybuild" -> {
        // go to policyEntryBuild class to do all the things
      }
      default -> Environment.sysOut("Unknown test type: " + type + ". No action taken.");
    }
  }
}
