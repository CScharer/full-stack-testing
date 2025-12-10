package com.cjs.qa.junit.tests;

import com.cjs.qa.core.Environment;
import com.cjs.qa.utilities.JavaHelpers;
import org.junit.Test;

public class TestSets {
  @Test
  public void emptyTest() {
    Environment.sysOut(
        "getCurrentPackageClassMethodDebugName:["
            + JavaHelpers.getCurrentPackageClassMethodDebugName()
            + "]");
    Environment.sysOut(
        "getCurrentPackageClassMethodName:["
            + JavaHelpers.getCurrentPackageClassMethodName()
            + "]");
    Environment.sysOut(
        "getCurrentPackageClassName:[" + JavaHelpers.getCurrentPackageClassName() + "]");
    Environment.sysOut("getCurrentPackageName:[" + JavaHelpers.getCurrentPackageName() + "]");
    Environment.sysOut(
        "getCurrentClassMethodDebugName:[" + JavaHelpers.getCurrentClassMethodDebugName() + "]");
    Environment.sysOut(
        "getCurrentClassMethodName:[" + JavaHelpers.getCurrentClassMethodName() + "]");
    Environment.sysOut("getCurrentClassName:[" + JavaHelpers.getCurrentClassName() + "]");
    Environment.sysOut(
        "getCurrentMethodDebugName:[" + JavaHelpers.getCurrentMethodDebugName() + "]");
    Environment.sysOut("getCurrentMethodName:[" + JavaHelpers.getCurrentMethodName() + "]");
  }
}
