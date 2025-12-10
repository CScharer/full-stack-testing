package com.cjs.qa.utilities;

import com.cjs.qa.core.Environment;
import com.cjs.qa.jdbc.JDBCConstants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class OdbcMsAccess64 {

  public void main(String[] args) {
    try (Connection oConnection =
        connectDb(
            "C:"
                + Constants.DELIMETER_PATH
                + "Temp"
                + Constants.DELIMETER_PATH
                + "qatoolsweb"
                + IExtension.MDB)) {
      if (oConnection != null) {
        try (Statement statement = oConnection.createStatement();
            ResultSet resultSet =
                statement.executeQuery(JDBCConstants.SELECT_ALL_FROM + "[tblSubmissionLog]")) {
          while (resultSet.next()) {
            // Environment.sysOut(resultSet.getString(1));
            Environment.sysOut("SubmissionID:" + resultSet.getString("SubmissionID"));
          }
        }
      }
    } catch (final Exception oException) {
      Environment.sysOut(oException.getMessage());
      oException.printStackTrace();
    }
  }

  public Connection connectDb(String database) {
    Environment.sysOut("Connecting to [" + database + "]");
    try {
      // String dir = System.getProperty("user.dir");
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      return DriverManager.getConnection(
          "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};Dbq=" + database, "", "");
    } catch (ClassNotFoundException | SQLException oException) {
      JOptionPane.showMessageDialog(null, "Problem connecting to database [" + database + "]");
      Environment.sysOut(oException.getMessage());
      oException.printStackTrace();
      return null;
    }
  }
}
