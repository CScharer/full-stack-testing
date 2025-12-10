package com.cjs.qa.junit.dataset;

import com.cjs.qa.core.Environment;
import com.cjs.qa.utilities.Constants;
import com.cjs.qa.utilities.IExtension;
import com.cjs.qa.utilities.JavaHelpers;
import java.io.FileReader;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.h2.Driver;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class BaseDBUnitTestForJPADao {
  private static EntityManagerFactory entityManagerFactory = null;
  private static IDatabaseConnection iDatabaseConnection = null;

  private EntityManager entityManager = null;

  protected static EntityManagerFactory getEntityManagerFactory() {
    return entityManagerFactory;
  }

  protected static IDatabaseConnection getiDatabaseConnection() {
    return iDatabaseConnection;
  }

  protected EntityManager getEntityManager() {
    return entityManager;
  }

  protected void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @BeforeClass
  public static void setupTestClass() throws Exception {
    Environment.sysOut("Setup-Class Method:[" + JavaHelpers.getCurrentClassName() + "]");
    final Properties properties = new Properties();
    properties.put("user", DBInfo.USER);
    properties.put("password", DBInfo.PASSWORD);

    try {
      final Connection connection = Driver.load().connect(DBInfo.URL, properties);
      iDatabaseConnection = new DatabaseConnection(connection);
      String dbLocation = Constants.PATH_PROJECT + "b2csite.dll" + IExtension.SQL;
      dbLocation = "src/test/resources/TableDef/b2csite.dll" + IExtension.SQL;
      RunScript.execute(iDatabaseConnection.getConnection(), new FileReader(dbLocation));
    } catch (final Exception e) {
      if (iDatabaseConnection != null) {
        iDatabaseConnection.close();
      }
      throw e;
    }

    final Map<Object, Object> mapProperties = new HashMap<>();
    mapProperties.put("javax.persistence.jdbc.url", DBInfo.URL);
    // mapProperties.put("hibernate.hbm2dll.auto", "create-drop");
    entityManagerFactory =
        Persistence.createEntityManagerFactory("orderPersistenceUnit", mapProperties);
  }

  @AfterClass
  public static void teardownTestClass() {
    Environment.sysOut("TearDown-Class Method:[" + JavaHelpers.getCurrentClassName() + "]");
    try {
      if (iDatabaseConnection != null) {
        iDatabaseConnection.close();
      }
      if (entityManagerFactory != null) {
        if (entityManagerFactory.isOpen()) {
          entityManagerFactory.close();
        }
        entityManagerFactory = null; // NOPMD - Test teardown in single-threaded context
      }
    } catch (final Exception e) {
      // Intentionally empty - cleanup failure is non-critical
      if (Environment.isLogAll()) {
        Environment.sysOut("Cleanup failure (non-critical): " + e.getMessage());
      }
    }
  }

  @Before
  public void baseSetup() throws Exception {
    setEntityManager(entityManagerFactory.createEntityManager());
  }

  @After
  public void baseTeardown() throws Exception {
    if (getEntityManager() != null) {
      if (getEntityManager().isOpen()) {
        getEntityManager().close();
      }
      setEntityManager(null);
    }
  }
}
