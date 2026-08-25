package ai.metayb.ui.core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CommonUtils {

    public static String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return currentDate.format(formatter);
    }

    public static String getUniqueNumber() {
        return String.valueOf(System.currentTimeMillis());
    }

}
