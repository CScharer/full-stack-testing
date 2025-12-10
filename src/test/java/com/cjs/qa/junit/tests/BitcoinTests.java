package com.cjs.qa.junit.tests;

import com.cjs.qa.bitcoin.Bitcoin;
import com.cjs.qa.core.Environment;
import com.cjs.qa.jdbc.JDBC;
import com.cjs.qa.jdbc.JDBCConstants;
import com.cjs.qa.maven.objects.TestRunCommand;
import com.cjs.qa.utilities.CommandLineTests;
import com.cjs.qa.utilities.Constants;
import com.cjs.qa.utilities.DateHelpersTests;
import com.cjs.qa.utilities.JavaHelpers;
import com.cjs.qa.vivit.VivitDataTests;
import java.math.BigDecimal;
import java.util.Map;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class BitcoinTests {
  @Rule public TestName testName = new TestName();
  private String mavenCommand =
      new TestRunCommand(this.getClass().getName(), getTestName()).toString();
  private JDBC jdbc = new JDBC("", VivitDataTests.DATABASE_DEFINITION);

  @Before
  public void beforeTestSetup() throws Throwable {
    Environment.sysOut(
        Constants.CLASS_METHOD_DEBUG
            + JavaHelpers.getCurrentClassMethodDebugName()
            + "], TestName"
            + ":["
            + getTestName()
            + "]");
    // mavenCommand = "mvn clean test " + getDArgLine() + "
    // -DfailIfNoTests=false -Dtest=" + this.getClass().getName() + "#" +
    // getTestName() + " -Dtags=" + Constants.QUOTE_DOUBLE + "@" +
    // Constants.QUOTE_DOUBLE
    mavenCommand = new TestRunCommand(this.getClass().getName(), getTestName()).toString();
  }

  @After
  public void afterTestTeardown() {
    Environment.sysOut(
        Constants.CLASS_METHOD_DEBUG
            + JavaHelpers.getCurrentClassMethodDebugName()
            + "], TestName"
            + ":["
            + getTestName()
            + "]");
  }

  public String getTestName() {
    return testName.getMethodName();
  }

  public JDBC getJdbc() {
    return jdbc;
  }

  public void setJdbc(JDBC jdbc) {
    this.jdbc = jdbc;
  }

  @Test
  public void mining() throws Throwable {
    Environment.sysOut(
        "getCurrentMethodDebugName:[" + JavaHelpers.getCurrentMethodDebugName() + "]");
    Environment.sysOut("mavenCommand:[" + mavenCommand + "]");
    String urlBitcoinCurrentPrice = "https://api.coindesk.com/v1/bpi/currentprice.json";
    String command = "curl -X GET " + urlBitcoinCurrentPrice;
    try {
      Bitcoin bitcoinPrevious = null;
      do {
        StringBuilder stringBuilderSQL = new StringBuilder();
        String dateTimeStamp =
            DateHelpersTests.getCurrentDateTime(DateHelpersTests.FORMAT_US_STANDARD_DATE_TIME);
        Map<String, String> responseMap = CommandLineTests.runProcess(command, true);
        String response = responseMap.get("lines");
        if (JavaHelpers.hasValue(response)) {
          // Environment.sysOut("response[" + response + "]")
          final JSONObject jsonObjectResponse = new JSONObject(response);
          final JSONObject jsonObjectBpi = jsonObjectResponse.getJSONObject("bpi");
          final JSONObject jsonObjectUSD = jsonObjectBpi.getJSONObject("USD");
          final String rate = jsonObjectUSD.getString("rate");
          final BigDecimal rateLong = jsonObjectUSD.getBigDecimal("rate_float");
          Bitcoin bitcoinCurrent = new Bitcoin(rate, rateLong, dateTimeStamp);
          if (bitcoinPrevious != null) {
            // Java 17: Records use direct accessor methods (no 'get' prefix)
            String bitcoinRateFloatCurrent = bitcoinCurrent.rateFloat().toString();
            String bitcoinRateFloatPrevious = bitcoinPrevious.rateFloat().toString();
            bitcoinPrevious = bitcoinCurrent;
            if (!bitcoinRateFloatCurrent.equals(bitcoinRateFloatPrevious)) {
              stringBuilderSQL = appendRecord(stringBuilderSQL, bitcoinCurrent);
              Environment.sysOut("bitcoinCurrent:[" + bitcoinCurrent.toString() + "]");
            }
          } else {
            bitcoinPrevious = bitcoinCurrent;
            stringBuilderSQL = appendRecord(stringBuilderSQL, bitcoinCurrent);
            Environment.sysOut("bitcoinCurrent:[" + bitcoinCurrent.toString() + "]");
          }
          // Java 17: Records use direct accessor methods (no 'get' prefix)
          Environment.sysOut(dateTimeStamp + "-rate:[$" + bitcoinCurrent.rate() + "]");
        } else {
          Environment.sysOut(dateTimeStamp + "-[No Response]");
        }
      } while (true);
    } catch (Exception e) {
      e.printStackTrace();
      if (getJdbc() != null) {
        getJdbc().close();
      }
    }
  }

  private StringBuilder appendRecord(StringBuilder stringBuilderSQL, Bitcoin bitcoin)
      throws Throwable {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(JDBCConstants.INSERT_INTO + " [t_Bitcoin] ");
    stringBuilder.append("([DateTimeStamp],[Rate],[RateFloat]");
    stringBuilder.append(") VALUES (");
    // Java 17: Records use direct accessor methods (no 'get' prefix)
    stringBuilder.append("'" + bitcoin.dateTimeStamp() + "',");
    stringBuilder.append("'" + bitcoin.rate() + "',");
    stringBuilder.append("'" + bitcoin.rateFloat() + "');");
    stringBuilder.append(Constants.NEWLINE);
    stringBuilderSQL.append(stringBuilder.toString());
    if (!stringBuilderSQL.toString().isEmpty()
        && stringBuilderSQL.toString().split(Constants.NEWLINE).length >= 1) {
      int recordsAffected = getJdbc().executeUpdate(stringBuilderSQL.toString(), false);
      Environment.sysOut("recordsAffected:[" + recordsAffected + "]");
      stringBuilderSQL = new StringBuilder();
    }
    return stringBuilderSQL;
  }
}
