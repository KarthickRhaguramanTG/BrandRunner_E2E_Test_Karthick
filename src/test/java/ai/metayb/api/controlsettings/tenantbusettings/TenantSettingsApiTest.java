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
 * GET /web/settings/tenant - "01. Control Settings / Tenant & BU Settings / Get Tenant Settings".
 *
 * SECURITY NOTE: this endpoint's real response contains live third-party integration
 * secrets (SMS/WhatsApp/email provider API keys and tokens - see settings values like
 * DISTRIBUTOR_INVOICE_SMS_CONFIG.apiKey, WHATSAPP.systemUserToken, EMAIL.emailPassword).
 * This is exactly why SanitizedApiLoggingFilter now also masks response-body fields
 * whose name contains key/token/secret/password, not just request headers. No assertion
 * below inspects or prints any secret value - only that the expected top-level settings
 * keys exist.
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Tenant & BU Settings")
public class TenantSettingsApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get tenant settings returns the tenant's integration configuration")
    @Story("Tenant Settings")
    @Description("Verified live: HTTP 200, 'Tenant settings fetched successfully', with known integration " +
            "setting groups present (values are never asserted or logged - see class Javadoc).")
    public void getTenantSettingsReturnsConfiguration() {
        Response response = given().spec(requestSpecification).get("/web/settings/tenant");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertEquals(response.jsonPath().getString("message"), "Tenant settings fetched successfully");
        Assert.assertNotNull(response.jsonPath().get("data.EMAIL"), "Should include an EMAIL settings group");
        Assert.assertNotNull(response.jsonPath().get("data.GOOGLE"), "Should include a GOOGLE settings group");
    }

    @Test(groups = { "regression", "negative"}, description = "Get tenant settings without authentication fails")
    @Story("Tenant Settings")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getTenantSettingsWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/settings/tenant");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
