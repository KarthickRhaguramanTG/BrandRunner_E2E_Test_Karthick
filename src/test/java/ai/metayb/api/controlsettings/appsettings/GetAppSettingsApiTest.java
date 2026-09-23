package ai.metayb.api.controlsettings.appsettings;

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
 * GET /web/settings/app-settings - "01. Control Settings / App Settings / Get App Settings".
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - App Settings")
public class GetAppSettingsApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get app settings returns stored settings and master data")
    @Story("App Settings")
    @Description("Verified live: HTTP 200 with data.masterData.locationHierarchies and data.storedData.appSettings/campaignWorkflow.")
    public void getAppSettingsReturnsStoredSettings() {
        Response response = given().spec(requestSpecification).get("/web/settings/app-settings");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertNotNull(response.jsonPath().get("data.masterData.locationHierarchies"), "Should include master data location hierarchies");
        Assert.assertNotNull(response.jsonPath().get("data.storedData.appSettings"), "Should include the stored app settings");
        Assert.assertNotNull(response.jsonPath().getString("data.storedData.appSettings.reconciliationPolicy"));
    }

    @Test(groups = { "regression", "negative"}, description = "Get app settings without authentication fails")
    @Story("App Settings")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getAppSettingsWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/settings/app-settings");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(groups = { "regression", "negative"}, description = "Get app settings authenticated but missing business_unit header fails")
    @Story("App Settings")
    @Description("Verified live: HTTP 401, message 'You do not have access to this business unit.' - same pattern " +
            "confirmed for Logout in the Auth & Session folder.")
    public void getAppSettingsWithoutBusinessUnitHeaderFails() {
        Response response = given()
                .filter(new ai.metayb.api.utils.SanitizedApiLoggingFilter())
                .baseUri(baseURI)
                .header("X-Amz-Tenant-Id", ai.metayb.config.ConfigManager.getApiTenant())
                .header("Authorization", "Bearer " + authToken)
                .get("/web/settings/app-settings");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "You do not have access to this business unit.");
    }
}
