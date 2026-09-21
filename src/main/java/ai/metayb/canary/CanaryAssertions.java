package ai.metayb.canary;

import io.restassured.response.Response;

import java.util.List;

/**
 * Tiny validation helpers mirroring the assertion style already proven in the
 * corresponding TestNG tests (status code + specific load-bearing response
 * fields, not just "did it return 200"). org.testng.Assert itself is not
 * available here - TestNG is a test-scope dependency and this class is
 * main-scope so it ships in the Canary artifact - so these throw a plain
 * IllegalStateException instead. Thrown from inside a
 * Synthetics.executeStep(...) Callable, that exception is exactly what fails
 * the step (and the overall Canary) per the SDK's documented behavior.
 *
 * Never includes the response body in a failure message - only the step
 * name, expected/actual status, and (for a missing-field failure) the field
 * path that was missing. None of the six smoke endpoints return credentials,
 * but keeping failure messages narrow is cheap insurance against a future
 * step accidentally doing so.
 */
final class CanaryAssertions {

    private CanaryAssertions() {
    }

    static void requireStatus(String stepName, Response response, int expectedStatus) {
        if (response.statusCode() != expectedStatus) {
            throw new IllegalStateException(stepName + " failed: expected HTTP " + expectedStatus
                    + " but got " + response.statusCode());
        }
    }

    static void requireField(String stepName, Response response, String jsonPath) {
        if (response.jsonPath().get(jsonPath) == null) {
            throw new IllegalStateException(stepName + " failed: response is missing required field '" + jsonPath + "'");
        }
    }

    static void requireNonEmptyList(String stepName, Response response, String jsonPath) {
        List<?> list = response.jsonPath().getList(jsonPath);
        if (list == null || list.isEmpty()) {
            throw new IllegalStateException(stepName + " failed: '" + jsonPath + "' should be a non-empty list");
        }
    }
}
