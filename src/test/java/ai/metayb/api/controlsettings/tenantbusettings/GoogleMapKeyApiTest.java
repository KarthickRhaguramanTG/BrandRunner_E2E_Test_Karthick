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
 * GET /web/settings/google-map-key - "01. Control Settings / Tenant & BU Settings / Get Google Map API Key".
 *
 * SECURITY NOTE: this endpoint's whole purpose is to hand back a real, live Google Maps
 * API key. The assertion below only checks presence/non-blankness - it never logs,
 * prints, or hardcodes the actual key value anywhere (including Allure). Response-body
 * logging for this call is masked by SanitizedApiLoggingFilter (field name contains "key").
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Tenant & BU Settings")
public class GoogleMapKeyApiTest extends BaseApiTest {

    @Test(groups = {"sanity", "regression", "positive"}, description = "Get Google Map API key returns a non-blank key")
    @Story("Google Map API Key")
    @Description("Verified live: HTTP 200, 'Google map key fetched successfully', data.googleMapKey non-blank. " +
            "The key value itself is never asserted, logged, or printed - only its presence.")
    public void getGoogleMapKeyReturnsNonBlankKey() {
        Response response = given().spec(requestSpecification).get("/web/settings/google-map-key");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertEquals(response.jsonPath().getString("message"), "Google map key fetched successfully");
        String key = response.jsonPath().getString("data.googleMapKey");
        Assert.assertNotNull(key, "Response should include data.googleMapKey");
        Assert.assertFalse(key.isBlank(), "data.googleMapKey must not be blank");
    }

    @Test(groups = { "regression", "negative"}, description = "Get Google Map API key without authentication fails")
    @Story("Google Map API Key")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getGoogleMapKeyWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/settings/google-map-key");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
