package ai.metayb.mobile.screens;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

/**
 * Parses a "strategy:value" locator spec (as stored in
 * config/mobile/locators.properties) into a real By. Keeping locators as
 * data in a properties file - rather than hardcoded By objects in each
 * screen class - is what makes screens configurable/example-based: swap the
 * file's values for a real app's real identifiers, no recompiling needed.
 */
final class MobileLocatorFactory {

    private MobileLocatorFactory() {
    }

    static By parse(String spec) {
        if (spec == null || spec.isBlank()) {
            throw new IllegalArgumentException("Locator spec must not be blank");
        }
        int separatorIndex = spec.indexOf(':');
        if (separatorIndex < 0) {
            throw new IllegalArgumentException("Locator spec must be 'strategy:value', got: " + spec);
        }
        String strategy = spec.substring(0, separatorIndex).trim().toLowerCase();
        String value = spec.substring(separatorIndex + 1).trim();

        return switch (strategy) {
            case "id" -> By.id(value);
            case "accessibility_id" -> AppiumBy.accessibilityId(value);
            case "xpath" -> By.xpath(value);
            case "class_name" -> By.className(value);
            case "android_uiautomator" -> AppiumBy.androidUIAutomator(value);
            case "ios_class_chain" -> AppiumBy.iOSClassChain(value);
            case "ios_predicate" -> AppiumBy.iOSNsPredicateString(value);
            default -> throw new IllegalArgumentException("Unsupported locator strategy: " + strategy);
        };
    }
}
