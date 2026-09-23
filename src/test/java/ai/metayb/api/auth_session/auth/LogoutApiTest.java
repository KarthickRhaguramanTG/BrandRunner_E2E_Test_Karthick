package ai.metayb.api.auth_session.auth;

import ai.metayb.api.utils.SanitizedApiLoggingFilter;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

import static io.restassured.RestAssured.given;

/**
 * POST /web/auth/logout - "00. Auth & Session / Auth / Logout".
 *
 * Tagged "noauth" in Postman, but verified live to actually require a valid
 * Authorization token AND the "business_unit" header - "noauth" in Postman only
 * means Postman won't auto-attach its collection-level bearer auth, not that the
 * endpoint accepts anonymous calls.
 *
 * Also verified live: this is a stateless-JWT API - logout does NOT invalidate the
 * access token server-side. The same token still works against /web/session
 * immediately after a "successful" logout. No test below asserts otherwise, since
 * that would assert something demonstrably false about this API.
 */
@Epic("BrandRunners Web API")
@Feature("Authentication")
public class LogoutApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Logout with a valid session succeeds")
    @Story("Logout")
    @Description("Verified live: HTTP 200, message 'Logged out successfully', using the suite's " +
            "authenticated requestSpecification (Authorization + business_unit + tenant).")
    public void logoutWithValidSessionSucceeds() {
        Response response = given().spec(requestSpecification)
                .contentType("application/json")
                .body("{}")
                .post("/web/auth/logout");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("message"), "Logged out successfully");
    }

    @Test(groups = {"sanity", "regression", "negative"}, description = "Logout without any authentication fails")
    @Story("Logout")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void logoutWithoutAuthenticationFails() {
        Response response = given().spec(noAuthRequestSpecification)
                .contentType("application/json")
                .body("{}")
                .post("/web/auth/logout");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(groups = { "regression", "negative"}, description = "Logout authenticated but missing the business_unit header fails")
    @Story("Logout")
    @Description("Verified live: HTTP 401, message 'You do not have access to this business unit.' - " +
            "distinct from the no-auth case, proving the token itself IS recognized and only the " +
            "business-unit scoping check fails.")
    public void logoutWithoutBusinessUnitHeaderFails() {
        Response response = given()
                .filter(new SanitizedApiLoggingFilter())
                .baseUri(baseURI)
                .header("X-Amz-Tenant-Id", ai.metayb.config.ConfigManager.getApiTenant())
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body("{}")
                .post("/web/auth/logout");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "You do not have access to this business unit.");
    }
}
