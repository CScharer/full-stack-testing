package com.cjs.qa.microsoft.excel;

import com.cjs.qa.core.Environment;
import com.cjs.qa.core.QAException;
import com.cjs.qa.microsoft.excel.xls.XLS;
import com.cjs.qa.microsoft.excel.xlsx.XLSX;
import com.cjs.qa.utilities.Constants;
import com.cjs.qa.utilities.FSOTests;
import com.cjs.qa.utilities.IExtension;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ExcelTests {
  private static final String SHEET_MISSING = "MISSING";

  @Test
  public void mainTest() throws IOException, QAException {
    final String filePath = Constants.PATH_DESKTOP + "Test" + Constants.DELIMETER_PATH;
    FileUtils.forceMkdir(new File(filePath));
    final List<String> fileTypeList = Arrays.asList(IExtension.XLS, IExtension.XLSX);
    for (final String fileType : fileTypeList) {
      final String filePathName = filePath + "ExcelTest" + fileType;
      FSOTests.fileDelete(filePathName);
      Environment.sysOut("filePathName:[" + filePathName + "]");
      switch (IExcel.getFileType(filePathName)) {
        case IExtension.XLSX:
          final XLSX xlsx = new XLSX(filePathName, null);
          testExcelType(xlsx);
          break;
        case IExtension.XLS:
        default:
          final XLS xls = new XLS(filePathName, null);
          testExcelType(xls);
          break;
      }
    }
  }

  private static void testExcelType(XLSX excel) throws QAException, IOException {
    excel.getFileName();
    excel.writeCell(IExcel.SHEET_DEFAULT, 0, 0, "Testing");
    excel.createSheet("String");
    excel.createHeadings("String", "String 1;String 2;String 3");
    Environment.sysOut(excel.readCell("String", 0, 0));
    Environment.sysOut(excel.readCell("String", 1, 0));
    Environment.sysOut(excel.readCell("String", 2, 0));
    excel.setFormatBold("String", 0, 0);
    excel.setFormatBold("String", 1, 0);
    excel.setFormatBold("String", 2, 0);
    excel.createSheet("List");
    excel.createHeadings("List", Arrays.asList("List 1", "List 2", "List 3"));
    Environment.sysOut(excel.readCell("List", 0, 0));
    Environment.sysOut(excel.readCell("List", 1, 0));
    Environment.sysOut(excel.readCell("List", 2, 0));
    excel.setFormatBold("List", 0, 0);
    excel.setFormatBold("List", 1, 0);
    excel.setFormatBold("List", 2, 0);
    Environment.sysOut("No Sheet Value:[" + excel.readCell("None", "List 4", 0) + "]");
    Environment.sysOut("No Sheet Value:[" + excel.readCell(9, "List 4", 0) + "]");
    Environment.sysOut("No Sheet Value:[" + excel.readCell(1, 9, 0) + "]");
    Environment.sysOut("No Row Value:[" + excel.readCell("List", "List 1", 9) + "]");
    Environment.sysOut("No Column Value:[" + excel.readCell("List", "List 4", 0) + "]");
    Environment.sysOut("No Column Value:[" + excel.readCell("List", 9, 0) + "]");
    excel.writeCell("New Sheet", 0, 0, "New Sheet 1");
    Environment.sysOut("Sheet Count:[" + excel.getSheetCount() + "]");
    Environment.sysOut(
        "Sheet Exists("
            + IExcel.SHEET_DEFAULT
            + "):["
            + excel.sheetExists(IExcel.SHEET_DEFAULT)
            + "]");
    Environment.sysOut("Sheet Exists(1):[" + excel.sheetExists(1) + "]");
    Environment.sysOut(
        "Sheet Exists(" + SHEET_MISSING + "):[" + excel.sheetExists(SHEET_MISSING) + "]");
    Environment.sysOut("Sheet Exists(5):[" + excel.sheetExists(5) + "]");
    //
    Environment.sysOut(
        "Row Exists("
            + Constants.QUOTE_DOUBLE
            + IExcel.SHEET_DEFAULT
            + Constants.QUOTE_DOUBLE
            + ", 0):["
            + excel.rowExists(IExcel.SHEET_DEFAULT, 0)
            + "]");
    Environment.sysOut("Row Exists(0, 0):[" + excel.rowExists(0, 0) + "]");
    Environment.sysOut(
        "Row Exists("
            + Constants.QUOTE_DOUBLE
            + IExcel.SHEET_DEFAULT
            + Constants.QUOTE_DOUBLE
            + ", 1):["
            + excel.rowExists(IExcel.SHEET_DEFAULT, 1)
            + "]");
    Environment.sysOut("Row Exists(0, 1):[" + excel.rowExists(0, 1) + "]");
    //
    Environment.sysOut(
        "Column Exists("
            + Constants.QUOTE_DOUBLE
            + IExcel.SHEET_DEFAULT
            + Constants.QUOTE_DOUBLE
            + ", "
            + Constants.QUOTE_DOUBLE
            + "New Sheet 1"
            + Constants.QUOTE_DOUBLE
            + ", 0):["
            + excel.columnExists(IExcel.SHEET_DEFAULT, "New Sheet 1", 0)
            + "]");
    Environment.sysOut(
        "Column Exists("
            + Constants.QUOTE_DOUBLE
            + IExcel.SHEET_DEFAULT
            + Constants.QUOTE_DOUBLE
            + ", 0, 0):["
            + excel.columnExists(IExcel.SHEET_DEFAULT, 0, 0)
            + "]");
    Environment.sysOut(
        "Column Exists(0, "
            + Constants.QUOTE_DOUBLE
            + "Testing"
            + Constants.QUOTE_DOUBLE
            + ", 0):["
            + excel.columnExists(0, "Testing", 0)
            + "]");
    Environment.sysOut("Column Exists(0, 0, 0):[" + excel.columnExists(0, 0, 0) + "]");
    //
    Environment.sysOut(
        "Column Exists("
            + SHEET_MISSING
            + ", "
            + Constants.QUOTE_DOUBLE
            + "New Sheet 1"
            + Constants.QUOTE_DOUBLE
            + ", 0):["
            + excel.columnExists(SHEET_MISSING, "New Sheet 1", 0)
            + "]");
    Environment.sysOut(
        "Column Exists("
            + Constants.QUOTE_DOUBLE
            + IExcel.SHEET_DEFAULT
            + Constants.QUOTE_DOUBLE
            + ", 0, 0):["
            + excel.columnExists(IExcel.SHEET_DEFAULT, 1, 0)
            + "]");
    Environment.sysOut(
        "Column Exists(0, "
            + Constants.QUOTE_DOUBLE
            + "New Sheet 1"
            + Constants.QUOTE_DOUBLE
            + ", 0):["
            + excel.columnExists(0, "New Sheet 1", 1)
            + "]");
    Environment.sysOut("Column Exists(0, 0, 0):[" + excel.columnExists(0, 1, 0) + "]");
    Environment.sysOut("Column Exists(0, 0, 0):[" + excel.columnExists(0, 0, 1) + "]");
    //
    Environment.sysOut(excel.readCell(IExcel.SHEET_DEFAULT, 0, 0));
    Environment.sysOut(
        "renameSheet(0, "
            + Constants.QUOTE_DOUBLE
            + "NewSheet"
            + Constants.QUOTE_DOUBLE
            + ")"
            + excel.renameSheet(0, "NewSheet"));
    Environment.sysOut(
        "renameSheet("
            + Constants.QUOTE_DOUBLE
            + "NewSheet"
            + Constants.QUOTE_DOUBLE
            + ", "
            + IExcel.SHEET_DEFAULT
            + ")"
            + excel.renameSheet("NewSheet", IExcel.SHEET_DEFAULT));
    // Environment.sysOut("renameSheet(9, " + Constants.QUOTE_DOUBLE +
    // "NewSheet" +
    // Constants.QUOTE_DOUBLE + ")" +
    // excel.renameSheet(9, "NewSheet"));
    Environment.sysOut(
        "renameSheet(1, "
            + Constants.QUOTE_DOUBLE
            + "NoNewSheet"
            + Constants.QUOTE_DOUBLE
            + ")"
            + excel.renameSheet(1, "NoNewSheet"));
    Environment.sysOut(
        "renameSheet("
            + Constants.QUOTE_DOUBLE
            + "NoNewSheet"
            + Constants.QUOTE_DOUBLE
            + ", "
            + Constants.QUOTE_DOUBLE
            + "NoSheet"
            + Constants.QUOTE_DOUBLE
            + ")"
            + excel.renameSheet("NoNewSheet", "NoSheet"));
    Environment.sysOut(
        "createSheet("
            + Constants.QUOTE_DOUBLE
            + "NoSheet"
            + Constants.QUOTE_DOUBLE
            + ")"
            + excel.createSheet("NoSheet"));
    Environment.sysOut("createSheet(" + SHEET_MISSING + ")" + excel.createSheet(SHEET_MISSING));
    Environment.sysOut("createSheet(" + SHEET_MISSING + ")" + excel.createSheet(SHEET_MISSING));
    Environment.sysOut("deleteSheet(" + SHEET_MISSING + ")" + excel.deleteSheet(SHEET_MISSING));
    Environment.sysOut("createSheet(" + SHEET_MISSING + ")" + excel.createSheet(SHEET_MISSING));
    Environment.sysOut("deleteSheet(1)" + excel.deleteSheet(1));
    // Environment.sysOut("deleteSheet(1)" + excel.deleteSheet(0));
    // Environment.sysOut("deleteSheet(1)" + excel.deleteSheet(0));
    // Environment.sysOut("deleteSheet(9)" + excel.deleteSheet(9));
    Environment.sysOut("createSheet(" + SHEET_MISSING + ")" + excel.createSheet(SHEET_MISSING));
    Environment.sysOut("deleteSheet(" + SHEET_MISSING + ")" + excel.deleteSheet(SHEET_MISSING));
    // Environment.sysOut("deleteSheet(" + SHEET_MISSING + ")" +
    // excel.deleteSheet(SHEET_MISSING));
    // Environment.sysOut(
    // "renameSheet(" + Constants.QUOTE_DOUBLE + "NoNewSheet" +
    // Constants.QUOTE_DOUBLE +
    // ", " + SHEET_MISSING + ")" +
    // excel.renameSheet("NoNewSheet", SHEET_MISSING));
    excel.setFormatBold("New Sheet", 0, 0);
    excel.writeCell("New Sheet", 1, 0, "1");
    excel.writeCell("New Sheet", 2, 0, "Column 3");
    excel.writeCell("New Sheet", 3, 0, "3");
    excel.writeCell("New Sheet", 4, 0, "Column 5");
    excel.writeCell("New Sheet", 5, 0, "5");
    // Environment.sysOut("File:[" + xls.getFileName() + "]");;
    // xls.close();
    //// Environment.sysOut(xls.readCell(xls.DEFAULT_SHEET, 0, 0));
    // FSOTests.fileDelete(fileName);
    // xls.convertToCSV(xls.getFileName() + IExtension.CSV);
    excel.autoSizeColumns("New Sheet");
    excel.save();
    excel.close();
  }

  private static void testExcelType(XLS excel) throws QAException, IOException {
    excel.getFileName();
    excel.writeCell(IExcel.SHEET_DEFAULT, 0, 0, "Testing");
    excel.createSheet("String");
    excel.createHeadings("String", "String 1;String 2;String 3");
    Environment.sysOut(excel.readCell("String", 0, 0));
    Environment.sysOut(excel.readCell("String", 1, 0));
    Environment.sysOut(excel.readCell("String", 2, 0));
    excel.setFormatBold("String", 0, 0);
    excel.setFormatBold("String", 1, 0);
    excel.setFormatBold("String", 2, 0);
    excel.createSheet("List");
    excel.createHeadings("List", Arrays.asList("List 1", "List 2", "List 3"));
    Environment.sysOut(excel.readCell("List", 0, 0));
    Environment.sysOut(excel.readCell("List", 1, 0));
    Environment.sysOut(excel.readCell("List", 2, 0));
    excel.setFormatBold("List", 0, 0);
    excel.setFormatBold("List", 1, 0);
    excel.setFormatBold("List", 2, 0);
    Environment.sysOut("No Sheet Value:[" + excel.readCell("None", "List 4", 0) + "]");
    Environment.sysOut("No Sheet Value:[" + excel.readCell(9, "List 4", 0) + "]");
    Environment.sysOut("No Sheet Value:[" + excel.readCell(1, 9, 0) + "]");
    Environment.sysOut("No Row Value:[" + excel.readCell("List", "List 1", 9) + "]");
    Environment.sysOut("No Column Value:[" + excel.readCell("List", "List 4", 0) + "]");
    Environment.sysOut("No Column Value:[" + excel.readCell("List", 9, 0) + "]");
    excel.writeCell("New Sheet", 0, 0, "New Sheet 1");
    Environment.sysOut("Sheet Count:[" + excel.getSheetCount() + "]");
    Environment.sysOut(
        "Sheet Exists("
            + IExcel.SHEET_DEFAULT
            + "):["
            + excel.sheetExists(IExcel.SHEET_DEFAULT)
            + "]");
    Environment.sysOut("Sheet Exists(1):[" + excel.sheetExists(1) + "]");
    Environment.sysOut(
        "Sheet Exists(" + SHEET_MISSING + "):[" + excel.sheetExists(SHEET_MISSING) + "]");
    Environment.sysOut("Sheet Exists(5):[" + excel.sheetExists(5) + "]");
    //
    Environment.sysOut(
        "Row Exists("
            + Constants.QUOTE_DOUBLE
            + IExcel.SHEET_DEFAULT
            + Constants.QUOTE_DOUBLE
            + ", 0):["
            + excel.rowExists(IExcel.SHEET_DEFAULT, 0)
            + "]");
    Environment.sysOut("Row Exists(0, 0):[" + excel.rowExists(0, 0) + "]");
    Environment.sysOut(
        "Row Exists("
            + Constants.QUOTE_DOUBLE
            + IExcel.SHEET_DEFAULT
            + Constants.QUOTE_DOUBLE
            + ", 1):["
            + excel.rowExists(IExcel.SHEET_DEFAULT, 1)
            + "]");
    Environment.sysOut("Row Exists(0, 1):[" + excel.rowExists(0, 1) + "]");
    //
    Environment.sysOut(
        "Column Exists("
            + Constants.QUOTE_DOUBLE
            + IExcel.SHEET_DEFAULT
            + Constants.QUOTE_DOUBLE
            + ", "
            + Constants.QUOTE_DOUBLE
            + "New Sheet 1"
            + Constants.QUOTE_DOUBLE
            + ", 0):["
            + excel.columnExists(IExcel.SHEET_DEFAULT, "New Sheet 1", 0)
            + "]");
    Environment.sysOut(
        "Column Exists("
            + Constants.QUOTE_DOUBLE
            + IExcel.SHEET_DEFAULT
            + Constants.QUOTE_DOUBLE
            + ", 0, 0):["
            + excel.columnExists(IExcel.SHEET_DEFAULT, 0, 0)
            + "]");
    Environment.sysOut(
        "Column Exists(0, "
            + Constants.QUOTE_DOUBLE
            + "Testing"
            + Constants.QUOTE_DOUBLE
            + ", 0):["
            + excel.columnExists(0, "Testing", 0)
            + "]");
    Environment.sysOut("Column Exists(0, 0, 0):[" + excel.columnExists(0, 0, 0) + "]");
    //
    Environment.sysOut(
        "Column Exists("
            + SHEET_MISSING
            + ", "
            + Constants.QUOTE_DOUBLE
            + "New Sheet 1"
            + Constants.QUOTE_DOUBLE
            + ", 0):["
            + excel.columnExists(SHEET_MISSING, "New Sheet 1", 0)
            + "]");
    Environment.sysOut(
        "Column Exists("
            + Constants.QUOTE_DOUBLE
            + IExcel.SHEET_DEFAULT
            + Constants.QUOTE_DOUBLE
            + ", 0, 0):["
            + excel.columnExists(IExcel.SHEET_DEFAULT, 1, 0)
            + "]");
    Environment.sysOut(
        "Column Exists(0, "
            + Constants.QUOTE_DOUBLE
            + "New Sheet 1"
            + Constants.QUOTE_DOUBLE
            + ", 0):["
            + excel.columnExists(0, "New Sheet 1", 1)
            + "]");
    Environment.sysOut("Column Exists(0, 0, 0):[" + excel.columnExists(0, 1, 0) + "]");
    Environment.sysOut("Column Exists(0, 0, 0):[" + excel.columnExists(0, 0, 1) + "]");
    //
    Environment.sysOut(excel.readCell(IExcel.SHEET_DEFAULT, 0, 0));
    Environment.sysOut(
        "renameSheet(0, "
            + Constants.QUOTE_DOUBLE
            + "NewSheet"
            + Constants.QUOTE_DOUBLE
            + ")"
            + excel.renameSheet(0, "NewSheet"));
    Environment.sysOut(
        "renameSheet("
            + Constants.QUOTE_DOUBLE
            + "NewSheet"
            + Constants.QUOTE_DOUBLE
            + ", "
            + IExcel.SHEET_DEFAULT
            + ")"
            + excel.renameSheet("NewSheet", IExcel.SHEET_DEFAULT));
    // Environment.sysOut("renameSheet(9, " + Constants.QUOTE_DOUBLE +
    // "NewSheet" +
    // Constants.QUOTE_DOUBLE + ")" +
    // excel.renameSheet(9, "NewSheet"));
    Environment.sysOut(
        "renameSheet(1, "
            + Constants.QUOTE_DOUBLE
            + "NoNewSheet"
            + Constants.QUOTE_DOUBLE
            + ")"
            + excel.renameSheet(1, "NoNewSheet"));
    Environment.sysOut(
        "renameSheet("
            + Constants.QUOTE_DOUBLE
            + "NoNewSheet"
            + Constants.QUOTE_DOUBLE
            + ", "
            + Constants.QUOTE_DOUBLE
            + "NoSheet"
            + Constants.QUOTE_DOUBLE
            + ")"
            + excel.renameSheet("NoNewSheet", "NoSheet"));
    Environment.sysOut(
        "createSheet("
            + Constants.QUOTE_DOUBLE
            + "NoSheet"
            + Constants.QUOTE_DOUBLE
            + ")"
            + excel.createSheet("NoSheet"));
    Environment.sysOut("createSheet(" + SHEET_MISSING + ")" + excel.createSheet(SHEET_MISSING));
    Environment.sysOut("createSheet(" + SHEET_MISSING + ")" + excel.createSheet(SHEET_MISSING));
    Environment.sysOut("deleteSheet(" + SHEET_MISSING + ")" + excel.deleteSheet(SHEET_MISSING));
    Environment.sysOut("createSheet(" + SHEET_MISSING + ")" + excel.createSheet(SHEET_MISSING));
    Environment.sysOut("deleteSheet(1)" + excel.deleteSheet(1));
    // Environment.sysOut("deleteSheet(1)" + excel.deleteSheet(0));
    // Environment.sysOut("deleteSheet(1)" + excel.deleteSheet(0));
    // Environment.sysOut("deleteSheet(9)" + excel.deleteSheet(9));
    Environment.sysOut("createSheet(" + SHEET_MISSING + ")" + excel.createSheet(SHEET_MISSING));
    Environment.sysOut("deleteSheet(" + SHEET_MISSING + ")" + excel.deleteSheet(SHEET_MISSING));
    // Environment.sysOut("deleteSheet(" + SHEET_MISSING + ")" +
    // excel.deleteSheet(SHEET_MISSING));
    // Environment.sysOut(
    // "renameSheet(" + Constants.QUOTE_DOUBLE + "NoNewSheet" +
    // Constants.QUOTE_DOUBLE +
    // ", " + SHEET_MISSING + ")" +
    // excel.renameSheet("NoNewSheet", SHEET_MISSING));
    excel.setFormatBold("New Sheet", 0, 0);
    excel.writeCell("New Sheet", 1, 0, "1");
    excel.writeCell("New Sheet", 2, 0, "Column 3");
    excel.writeCell("New Sheet", 3, 0, "3");
    excel.writeCell("New Sheet", 4, 0, "Column 5");
    excel.writeCell("New Sheet", 5, 0, "5");
    // Environment.sysOut("File:[" + xls.getFileName() + "]");;
    // xls.close();
    //// Environment.sysOut(xls.readCell(xls.DEFAULT_SHEET, 0, 0));
    // FSOTests.fileDelete(fileName);
    // xls.convertToCSV(xls.getFileName() + IExtension.CSV);
    excel.autoSizeColumns("New Sheet");
    excel.save();
    excel.close();
  }
}
