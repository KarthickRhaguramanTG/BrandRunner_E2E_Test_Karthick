package ai.metayb.api.client;

import ai.metayb.api.utils.SanitizedApiLoggingFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * The minimum reusable login/session logic the CloudWatch Canary needs -
 * extracted from {@code testUtils.BaseApiTest} rather than reusing that class
 * directly.
 *
 * BaseApiTest can't be reused as-is for two independent reasons: (1) it lives
 * under src/test/java, so a plain `mvn package` never puts it in the runtime
 * jar a Lambda-based Canary loads; (2) its auth state (authToken,
 * businessUnitId) is static and populated once per TestNG suite run via
 * {@code @BeforeSuite} - exactly the "static TestNG authentication state" a
 * Canary must NOT reuse, since each Canary invocation needs its own fresh
 * login. BaseApiTest itself is intentionally left untouched; this class
 * duplicates its ~20 lines of login logic rather than risk touching a base
 * class every existing API test extends.
 */
public final class CanaryApiSessionClient {

    private CanaryApiSessionClient() {
    }

    /**
     * POST /web/auth/login (BrandRunners Web APIs Postman collection,
     * "00. Auth & Session / Auth / Login") - same request BaseApiTest.performLogin
     * makes, reproduced here so it's callable from main-scope code.
     */
    public static ApiSession login(String baseUri, String tenant, String email, String password) throws Exception {
        Response response = given()
                .filter(new SanitizedApiLoggingFilter())
                .baseUri(baseUri)
                .header("X-Amz-Tenant-Id", tenant)
                .contentType("application/json")
                .body(loginPayload(email, password))
                .post("/web/auth/login");

        String authToken = response.getCookie("accessToken");
        if (authToken == null || authToken.isEmpty()) {
            throw new IllegalStateException(
                    "Login failed - no accessToken cookie in response (status " + response.statusCode() + ")");
        }

        String businessUnitId = response.jsonPath().getString("data.user.businessInfo[0].id");
        if (businessUnitId == null || businessUnitId.isEmpty()) {
            throw new IllegalStateException("Login succeeded but no business unit id was returned");
        }

        return new ApiSession(authToken, businessUnitId);
    }

    /**
     * Same header shape as BaseApiTest.requestSpecification: tenant + bearer
     * token + business_unit, with the existing secret-masking filter attached.
     */
    public static RequestSpecification buildAuthenticatedRequestSpec(String baseUri, String tenant, ApiSession session) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .addHeader("X-Amz-Tenant-Id", tenant)
                .addHeader("Authorization", "Bearer " + session.authToken())
                .addHeader("business_unit", session.businessUnitId())
                .addFilter(new SanitizedApiLoggingFilter())
                .build();
    }

    private static String loginPayload(String email, String password) throws Exception {
        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("email", email);
        payload.put("password", password);
        return new ObjectMapper().writeValueAsString(payload);
    }
}
