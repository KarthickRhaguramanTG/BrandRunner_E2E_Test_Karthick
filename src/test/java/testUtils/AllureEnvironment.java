package testUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static testUtils.BaseTest.browser;

public class AllureEnvironment {

    public static void createEnvironmentFile() {

        try(InputStream input = AllureEnvironment.class.getClassLoader()
                .getResourceAsStream("config/config.properties")){

        if (input == null) {
            throw new RuntimeException("config/config.properties not found in classpath!");
        }

        Properties configProps = new Properties();
        configProps.load(input);

        Properties envProps = new Properties();
        envProps.setProperty("Project Name", configProps.getProperty("projectName", "IFC Project"));
        envProps.setProperty("Environment", configProps.getProperty("environment", "QA"));
        envProps.setProperty("Browser", configProps.getProperty("ui.browser", browser));

        // Ensure target/allure-results exists
        File allureResultsDir = new File("target/allure-results");
        if (!allureResultsDir.exists()) {
            allureResultsDir.mkdirs();
        }

        // Write to allure-results/environment.properties
        try (FileOutputStream fos = new FileOutputStream("target/allure-results/environment.properties")) {
            envProps.store(fos, "Allure Environment Properties");
        }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
