package ai.metayb.ui.core;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;

public class FileUtils {

    /**
     * Returns a File object for a file inside src/main/resources/test-data.
     * Example: "image.jpg"
     */
    public static File getFile(String resourcePath) {
        URL resource = FileUtils.class.getClassLoader().getResource(resourcePath);

        if (resource == null) {
            throw new RuntimeException("Resource not found: " + resourcePath);
        }

        try {
            return Paths.get(resource.toURI()).toFile();
        } catch (Exception e) {
            throw new RuntimeException("Unable to load file: " + resourcePath, e);
        }
    }

    /**
     * Returns absolute path (useful for Selenium file upload).
     */
    public static String getAbsolutePath(String resourcePath) {
        return getFile(resourcePath).getAbsolutePath();
    }

    /**
     * Returns InputStream for reading content (CSV/JSON/XML…)
     */
    public static InputStream getStream(String resourcePath) {
        InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(resourcePath);

        if (in == null) {
            throw new RuntimeException("Resource not found: " + resourcePath);
        }

        return in;
    }

    public final String imagePath = FileUtils.getAbsolutePath("test-data/image.jpg");
    public final File imageFile = FileUtils.getFile("test-data/image.jpg");
    public final InputStream imageStream = FileUtils.getStream("test-data/image.jpg");

    public final String xlsxPath = FileUtils.getAbsolutePath("test-data/Coupons_Sample.xlsx");
    public final File xlsxFile = FileUtils.getFile("test-data/Coupons_Sample.xlsx");
    public final InputStream xlsxStream = FileUtils.getStream("test-data/Coupons_Sample.xlsx");

}
