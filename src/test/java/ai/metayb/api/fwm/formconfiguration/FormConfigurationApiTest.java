package ai.metayb.api.fwm.formconfiguration;

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
 * "03. FWM / Form Configuration" - both Postman requests, kept in one class per
 * Section 16 ("avoid unnecessary classes for simple APIs").
 *
 * *** APPLICATION-SIDE FINDING *** "Get Form Configuration" (called exactly as the
 * Postman collection defines it, FORMTYPE=INITIATION) returns HTTP 500 with a raw
 * SQL query fragment in the error message ("Failed query: select \"id\", " +
 * "\"business_unit_id\", ...") - a real backend bug leaking database schema details
 * in an error response, not something this automation framework can or should fix.
 * The same class of issue (raw SQL leaking via a 500) recurs at "03. FWM / Gallery /
 * Get User Info" - see GalleryApiTest.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Form Configuration")
public class FormConfigurationApiTest extends BaseApiTest {

    @Test(groups = {"api", "regression"}, description = "Get Form Configuration, called exactly as the Postman collection defines it, currently errors server-side")
    @Story("Get Form Configuration")
    @Description("Verified live: HTTP 500 with a raw SQL query fragment in the error message - a real backend " +
            "bug (see class Javadoc), documented as observed behavior per Step 8 rather than asserting a " +
            "successful response that this environment does not currently produce.")
    public void getFormConfigurationAsDefinedInPostmanErrors() {
        Response response = given().spec(requestSpecification).get("/web/form-config?FORMTYPE=INITIATION");

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), false);
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get Form Configuration without authentication fails")
    @Story("Get Form Configuration")
    @Description("Verified live: HTTP 401, message 'Authentication token missing' - the auth check happens " +
            "before the query that produces the 500 above, so this negative case is unaffected by that bug.")
    public void getFormConfigurationWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/form-config?FORMTYPE=INITIATION");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: PUT /web/form-config/upsert-form-config returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Upsert Form Configuration")
    @Description("Blocked by the same systemic environment issue confirmed throughout this folder - directly verified live. Not executed.")
    public void upsertFormConfigurationNotAutomatedDueToEnvironmentIssue() {
    }
}
