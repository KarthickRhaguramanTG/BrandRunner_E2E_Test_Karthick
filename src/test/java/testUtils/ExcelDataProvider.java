package testUtils;

import ai.metayb.ui.core.ExcelUtils;
import org.testng.annotations.DataProvider;

public class ExcelDataProvider {

    @DataProvider(name = "excelData")
    public Object[][] excelDataProvider() {

        String path = "test-data/data.xlsx";
        String sheet = "Sheet1";

        var excelData = ExcelUtils.readExcel(path, sheet);

        Object[][] data = new Object[excelData.size()][1];

        for (int i = 0; i < excelData.size(); i++) {
            data[i][0] = excelData.get(i);   // pass Map<String,String>
        }

        return data;
    }
}
