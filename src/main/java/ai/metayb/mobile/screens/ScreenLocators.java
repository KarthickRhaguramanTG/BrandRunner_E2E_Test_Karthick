package ai.metayb.mobile.screens;

import ai.metayb.mobile.config.MobileConfigManager;
import ai.metayb.mobile.config.MobilePlatform;
import org.openqa.selenium.By;

/**
 * Resolves a screen locator from config/mobile/locators.properties, picking
 * the ".android" or ".ios" suffix for the current platform automatically -
 * screen classes ask for "login.username.field" and never see the platform
 * split.
 */
public final class ScreenLocators {

    private ScreenLocators() {
    }

    public static By resolve(String baseKey) {
        String platformSuffix = MobileConfigManager.getPlatform() == MobilePlatform.ANDROID ? "android" : "ios";
        String spec = MobileConfigManager.getRequired(baseKey + "." + platformSuffix);
        return MobileLocatorFactory.parse(spec);
    }
}
