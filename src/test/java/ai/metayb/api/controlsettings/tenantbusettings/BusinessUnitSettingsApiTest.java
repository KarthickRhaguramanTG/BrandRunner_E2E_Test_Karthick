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
 * GET /web/settings/business-unit - "01. Control Settings / Tenant & BU Settings / Get Business Unit Settings".
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Tenant & BU Settings")
public class BusinessUnitSettingsApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "Get business unit settings returns the BU's configuration")
    @Story("Business Unit Settings")
    @Description("Verified live: HTTP 200, 'Business unit settings fetched successfully', with a CURRENCY settings group present.")
    public void getBusinessUnitSettingsReturnsConfiguration() {
        Response response = given().spec(requestSpecification).get("/web/settings/business-unit");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertEquals(response.jsonPath().getString("message"), "Business unit settings fetched successfully");
        Assert.assertNotNull(response.jsonPath().get("data.CURRENCY"), "Should include a CURRENCY settings group");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get business unit settings without authentication fails")
    @Story("Business Unit Settings")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getBusinessUnitSettingsWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/settings/business-unit");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
