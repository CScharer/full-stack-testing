package com.cjs.qa.jdbc;

import com.cjs.qa.vivit.VivitDataTests;
import org.junit.Test;

public class JDBCTest {

  @Test
  public void getDatabaseTablesViewsFields() throws Throwable {
    JDBC.exportTableViewSchemaSQLite("TablesViews", VivitDataTests.DATABASE_DEFINITION, true);
  }
}
