package com.cjs.qa.junit.tests;

import com.cjs.qa.core.Environment;
import com.cjs.qa.jdbc.EDBDriver;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class EDBDriverTests {
  private final List<String> databaseTypeList =
      Arrays.asList("DB2", "MICROSOFT", "QUICKBOOKS", "SQLITE", "SQLSERVER", "UCANACCESS");

  private List<String> getDatabaseTypeList() {
    return databaseTypeList;
  }

  @Test
  public void edbDrivers() {
    for (final String databaseType : getDatabaseTypeList()) {
      final EDBDriver eDBDriver = EDBDriver.fromString(databaseType);
      Environment.sysOut(
          "Driver Type:["
              + databaseType
              + "], JDBC Driver:["
              + eDBDriver.getJdbcDriver()
              + "], URL Prefix["
              + eDBDriver.getUrlPrefix()
              + "]");
    }
  }
}
