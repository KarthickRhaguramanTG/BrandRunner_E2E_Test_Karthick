package ai.metayb.api.controlsettings.tenantbusettings;

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
 * GET /web/settings/ai-integration-status - "01. Control Settings / Tenant & BU Settings / Get AI Integration Status".
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Tenant & BU Settings")
public class AiIntegrationStatusApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "Get AI integration status returns whether AI is enabled")
    @Story("AI Integration Status")
    @Description("Verified live: HTTP 200, 'AI integration status fetched successfully', data.aiEnabled boolean present.")
    public void getAiIntegrationStatusReturnsFlag() {
        Response response = given().spec(requestSpecification).get("/web/settings/ai-integration-status");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertEquals(response.jsonPath().getString("message"), "AI integration status fetched successfully");
        Assert.assertNotNull(response.jsonPath().get("data.aiEnabled"), "Response should include data.aiEnabled");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get AI integration status without authentication fails")
    @Story("AI Integration Status")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getAiIntegrationStatusWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/settings/ai-integration-status");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
