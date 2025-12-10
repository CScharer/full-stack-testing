package com.cjs.qa.junit.tests;

import com.cjs.qa.core.Environment;
import com.cjs.qa.microsoft.excel.IExcel;
import com.cjs.qa.utilities.Convert;
import com.cjs.qa.utilities.JavaHelpers;
import io.cucumber.datatable.DataTable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class ConvertTests {
  private static DataTable getDataTable() {
    return DataTable.create(getList());
  }

  private static List<List<String>> getList() {
    return Arrays.asList(
        Arrays.asList("One", "1"),
        Arrays.asList("Two", "2"),
        Arrays.asList("Three", ""),
        Arrays.asList("Four", "4"),
        Arrays.asList("Five", "5"),
        Arrays.asList("Six", "6"),
        Arrays.asList("Seven", ""),
        Arrays.asList("Eight", "8"),
        Arrays.asList("Nine", "9"),
        Arrays.asList("Ten", "10"));
  }

  private static Map<String, String> getMap() {
    final Map<String, String> map = new HashMap<>();
    map.put("One", "1");
    map.put("Two", "2");
    map.put("Three", "");
    map.put("Four", "4");
    map.put("Five", "5");
    map.put("Six", "6");
    map.put("Seven", "");
    map.put("Eight", "8");
    map.put("Nine", "9");
    map.put("Ten", "10");
    return map;
  }

  @Test
  public void fromDataTableToList() {
    final String method = JavaHelpers.getCurrentMethodName();
    Environment.sysOut("method:[" + method + "]");
    Environment.sysOut(method);
    final DataTable dataTable = getDataTable();
    List<List<String>> listList;
    listList = Convert.fromDataTableToList(dataTable);
    Environment.sysOut(method + ":[" + listList.toString() + "]");
    listList = Convert.fromDataTableToList(dataTable, true);
    Environment.sysOut(method + ", true:[" + listList.toString() + "]");
    listList = Convert.fromDataTableToList(dataTable, false);
    Environment.sysOut(method + ", false:[" + listList.toString() + "]");
  }

  @Test
  public void fromDataTableToMap() {
    final String method = JavaHelpers.getCurrentMethodName();
    Environment.sysOut("method:[" + method + "]");
    final DataTable dataTable = getDataTable();
    Map<String, String> map;
    map = Convert.fromDataTableToMap(dataTable);
    Environment.sysOut(method + ":[" + map.toString() + "]");
    map = Convert.fromDataTableToMap(dataTable, true);
    Environment.sysOut(method + ", true:[" + map.toString() + "]");
    map = Convert.fromDataTableToMap(dataTable, false);
    Environment.sysOut(method + ", false:[" + map.toString() + "]");
  }

  @Test
  public void fromListToDataTable() {
    final String method = JavaHelpers.getCurrentMethodName();
    Environment.sysOut("method:[" + method + "]");
    final List<List<String>> listList = getList();
    DataTable dataTable;
    dataTable = Convert.fromListToDataTable(listList);
    Environment.sysOut(method + ":[" + dataTable.toString() + "]");
    dataTable = Convert.fromListToDataTable(listList, true);
    Environment.sysOut(method + ", true:[" + dataTable.toString() + "]");
    dataTable = Convert.fromListToDataTable(listList, false);
    Environment.sysOut(method + ", false:[" + dataTable.toString() + "]");
  }

  @Test
  public void fromListToMap() {
    final String method = JavaHelpers.getCurrentMethodName();
    Environment.sysOut("method:[" + method + "]");
    final List<List<String>> listList = getList();
    Map<String, String> map;
    map = Convert.fromListToMap(listList);
    Environment.sysOut(method + ":[" + map.toString() + "]");
    map = Convert.fromListToMap(listList, true);
    Environment.sysOut(method + ", true:[" + map.toString() + "]");
    map = Convert.fromListToMap(listList, false);
    Environment.sysOut(method + ", false:[" + map.toString() + "]");
  }

  @Test
  public void fromMapToDataTable() {
    final String method = JavaHelpers.getCurrentMethodName();
    Environment.sysOut("method:[" + method + "]");
    final Map<String, String> map = getMap();
    Environment.sysOut("map:[" + map.toString() + "]");
    DataTable dataTable;
    dataTable = Convert.fromMapToDataTable(map);
    Environment.sysOut(method + ":[" + dataTable.toString() + "]");
    dataTable = Convert.fromMapToDataTable(map, true);
    Environment.sysOut(method + ", true:[" + dataTable.toString() + "]");
    dataTable = Convert.fromMapToDataTable(map, false);
    Environment.sysOut(method + ", false:[" + dataTable.toString() + "]");
  }

  @Test
  public void fromMapToList() {
    final String method = JavaHelpers.getCurrentMethodName();
    Environment.sysOut("method:[" + method + "]");
    final Map<String, String> map = getMap();
    Environment.sysOut("map:[" + map.toString() + "]");
    List<List<String>> listList;
    listList = Convert.fromMapToList(map);
    Environment.sysOut(method + ":[" + listList.toString() + "]");
    listList = Convert.fromMapToList(map, true);
    Environment.sysOut(method + ", true:[" + listList.toString() + "]");
    listList = Convert.fromMapToList(map, false);
    Environment.sysOut(method + ", false:[" + listList.toString() + "]");
  }

  @Test
  public void fromNumberToLetter() {
    final String method = JavaHelpers.getCurrentMethodName();
    Environment.sysOut("method:[" + method + "]");
    for (int number = -1; number <= 26; number++) {
      Environment.sysOut(
          "number:[" + number + "], letter:[" + Convert.fromNumberToLetter(number) + "]");
    }
  }

  @Test
  public void fromNumberToLetterExcel() {
    final String method = JavaHelpers.getCurrentMethodName();
    final String formatNumber = "###,###,##0";
    final int numberStep = 1;
    final int numberStart = -1;
    final int numberEnd = IExcel.MAX_COLUMNS_XLSX + 1;
    Environment.sysOut("method:[" + method + "]");
    for (int number = numberStart; number <= numberEnd; number += numberStep) {
      Environment.sysOut(
          "number:["
              + JavaHelpers.formatNumber(number, formatNumber)
              + "], letter:["
              + Convert.fromNumberToLetterExcel(number)
              + "]");
    }
  }
}
