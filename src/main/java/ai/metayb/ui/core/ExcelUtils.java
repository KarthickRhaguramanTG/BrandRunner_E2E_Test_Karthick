package ai.metayb.ui.core;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcelUtils {

    public static List<Map<String, String>> readExcel(String filePath, String sheetName) {
        List<Map<String, String>> rows = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet '" + sheetName + "' not found in " + filePath);
            }

            // headers
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IllegalArgumentException("Sheet '" + sheetName + "' in " + filePath + " has no header row");
            }
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.toString());
            }

            // rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowMap = new HashMap<>();

                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    String value = (cell == null) ? "" : cell.toString();
                    rowMap.put(headers.get(j), value);
                }
                rows.add(rowMap);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }

        return rows;
    }

    /**
     * Writes a list of rows (Map<columnName, value>) to a new Excel file.
     * Headers are inferred from the union of all keys (order is iteration order of LinkedHashSet).
     */
    public static void writeExcel(List<Map<String, String>> rows, String filePath, String sheetName) throws IOException {
        // Collect headers in insertion order
        Set<String> headerSet = new LinkedHashSet<>();
        for (Map<String, String> row : rows) {
            headerSet.addAll(row.keySet());
        }
        List<String> headers = new ArrayList<>(headerSet);
        writeExcel(rows, headers, filePath, sheetName);
    }

    /**
     * Writes rows to an Excel file using a specified header order.
     */
    public static void writeExcel(List<Map<String, String>> rows, List<String> headers, String filePath, String sheetName) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName == null ? "Sheet1" : sheetName);

            // Create header row with bold font
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }

            // Write data rows
            for (int r = 0; r < rows.size(); r++) {
                Row sheetRow = sheet.createRow(r + 1); // +1 because 0 is header
                Map<String, String> data = rows.get(r);
                for (int c = 0; c < headers.size(); c++) {
                    String header = headers.get(c);
                    String value = data.getOrDefault(header, "");
                    Cell cell = sheetRow.createCell(c);
                    cell.setCellValue(value);
                }
            }

            // Autosize columns
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }
}
