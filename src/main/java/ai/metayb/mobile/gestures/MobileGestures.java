package ai.metayb.mobile.gestures;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.List;

/**
 * Gesture helpers built on W3C Actions (PointerInput/Sequence) - the current
 * correct approach for Appium 2 / Selenium 4. The older TouchAction /
 * MultiTouchAction API is deprecated and removed from this client version,
 * so it is intentionally not used here.
 */
public class MobileGestures {

    private static final Duration DEFAULT_SWIPE_DURATION = Duration.ofMillis(400);

    private MobileGestures() {
    }

    public static void tap(AppiumDriver driver, WebElement element) {
        Point center = centerOf(element);
        tap(driver, center.getX(), center.getY());
    }

    public static void tap(AppiumDriver driver, int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(List.of(tap));
    }

    public static void longPress(AppiumDriver driver, WebElement element, Duration holdDuration) {
        Point center = centerOf(element);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence longPress = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), center.getX(), center.getY()))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, holdDuration))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(List.of(longPress));
    }

    public static void swipe(AppiumDriver driver, int startX, int startY, int endX, int endY) {
        swipe(driver, startX, startY, endX, endY, DEFAULT_SWIPE_DURATION);
    }

    public static void swipe(AppiumDriver driver, int startX, int startY, int endX, int endY, Duration duration) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(duration, PointerInput.Origin.viewport(), endX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(List.of(swipe));
    }

    public static void scrollDown(AppiumDriver driver) {
        Dimension size = driver.manage().window().getSize();
        int startX = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * 0.8);
        int endY = (int) (size.getHeight() * 0.2);
        swipe(driver, startX, startY, startX, endY);
    }

    public static void scrollUp(AppiumDriver driver) {
        Dimension size = driver.manage().window().getSize();
        int startX = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * 0.2);
        int endY = (int) (size.getHeight() * 0.8);
        swipe(driver, startX, startY, startX, endY);
    }

    private static Point centerOf(WebElement element) {
        Point location = element.getLocation();
        Dimension size = element.getSize();
        return new Point(location.getX() + size.getWidth() / 2, location.getY() + size.getHeight() / 2);
    }
}
