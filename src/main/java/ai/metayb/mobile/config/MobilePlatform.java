package ai.metayb.mobile.config;

public enum MobilePlatform {
    ANDROID,
    IOS;

    public static MobilePlatform fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Mobile platform must be specified (e.g. 'android' or 'ios')");
        }
        try {
            return MobilePlatform.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported mobile platform: " + value, e);
        }
    }
}
