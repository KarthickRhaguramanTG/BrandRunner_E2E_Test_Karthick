package ai.metayb.ui;

import ai.metayb.ui.core.ExcelUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import testUtils.BaseTest;
import testUtils.ExcelDataProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelUploadTest extends BaseTest {

    public static final List<Map<String, String>> results = new ArrayList<>();

    @Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
    public void testExample(Map<String, String> data) {
        boolean passed = true;
        String note = "";

        try {
            // ...test steps
        } catch (Exception e) {
            passed = false;
            note = e.getMessage();
        }

        Map<String, String> row = new LinkedHashMap<>(data);
        row.put("Result", passed ? "PASS" : "FAIL");
        row.put("Note", note);
        results.add(row);
    }

    @AfterClass
    public void writeResultsToExcel() {
        String outFile = "test-data/test-results.xlsx";
        try {
            ExcelUtils.writeExcel(results, outFile, "Results");
            System.out.println("Results written to " + outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
