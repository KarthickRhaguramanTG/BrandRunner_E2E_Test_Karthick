package ai.metayb.api.auth_session.session;

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
 * GET /web/session - "00. Auth & Session / Session / Get Current Session".
 * Requires Authorization + X-Amz-Tenant-Id + business_unit (per Postman headers).
 */
@Epic("BrandRunners Web API")
@Feature("Session")
public class CurrentSessionApiTest extends BaseApiTest {

    @Test(groups = {"sanity", "regression", "positive"}, description = "Get current session returns the logged-in user's session details")
    @Story("Current Session")
    @Description("Verified live: HTTP 200 with id/name/email/roleId/roleName/businessUnitId/permissions.")
    public void getCurrentSessionReturnsSessionDetails() {
        Response response = given().spec(requestSpecification).get("/web/session");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertNotNull(response.jsonPath().get("data.id"), "Session should include a user id");
        Assert.assertNotNull(response.jsonPath().getString("data.email"), "Session should include the user's email");
        Assert.assertNotNull(response.jsonPath().getString("data.roleName"), "Session should include the user's role name");
        Assert.assertFalse(response.jsonPath().getList("data.permissions").isEmpty(), "Session should include a non-empty permissions list");
    }

    @Test(groups = {"sanity", "regression", "negative"}, description = "Get current session without an Authorization header fails")
    @Story("Current Session")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getCurrentSessionWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/session");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(groups = { "regression", "negative"}, description = "Get current session with an invalid bearer token fails")
    @Story("Current Session")
    @Description("Verified live: HTTP 401, message 'Invalid or expired token' - distinct from the missing-token message.")
    public void getCurrentSessionWithInvalidTokenFails() {
        Response response = given()
                .filter(new SanitizedApiLoggingFilter())
                .baseUri(baseURI)
                .header("X-Amz-Tenant-Id", ai.metayb.config.ConfigManager.getApiTenant())
                .header("business_unit", businessUnitId)
                .header("Authorization", "Bearer invalid.token.value")
                .get("/web/session");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid or expired token");
    }
}
