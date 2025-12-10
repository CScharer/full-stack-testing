package com.cjs.qa.bts.policy;

import com.cjs.qa.core.Environment;
import org.junit.Test;

public final class PolicyTestCase {
  private PolicyTestCase() { // Private constructor
  }

  @Test
  public static void mainTest() { // main(String[] args)
    final Policy policy = new Policy("123456789-10");
    Environment.sysOut(policy.getPolicy());
    Environment.sysOut(policy.getPolicyNumber());
    Environment.sysOut(policy.getSequenceNumber());
  }
}
