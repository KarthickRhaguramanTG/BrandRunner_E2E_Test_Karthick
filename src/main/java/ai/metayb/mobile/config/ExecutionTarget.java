package ai.metayb.mobile.config;

public enum ExecutionTarget {
    LOCAL_EMULATOR,
    LOCAL_SIMULATOR,
    PHYSICAL_DEVICE,
    BROWSERSTACK;

    public static ExecutionTarget fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Execution target must be specified (e.g. 'emulator', 'simulator', 'physical', 'browserstack')");
        }
        switch (value.trim().toUpperCase()) {
            case "EMULATOR":
            case "LOCAL_EMULATOR":
                return LOCAL_EMULATOR;
            case "SIMULATOR":
            case "LOCAL_SIMULATOR":
                return LOCAL_SIMULATOR;
            case "PHYSICAL":
            case "PHYSICAL_DEVICE":
            case "DEVICE":
                return PHYSICAL_DEVICE;
            case "BROWSERSTACK":
            case "CLOUD":
                return BROWSERSTACK;
            default:
                throw new IllegalArgumentException("Unsupported execution target: " + value);
        }
    }
}
