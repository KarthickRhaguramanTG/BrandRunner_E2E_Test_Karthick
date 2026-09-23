package ai.metayb.api.controlsettings.businessunits;

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
 * "01. Control Settings / Business Units" - read requests: Get All, Get Business Unit by ID.
 *
 * *** CRITICAL SECURITY FINDING (application-side, not a test/framework issue) ***
 * GET /web/business-unit/get-all was found live to return an "env" object in its
 * response body containing this backend's real AWS_ACCESS_KEY_ID AND
 * AWS_SECRET_ACCESS_KEY - i.e. a live, long-lived AWS credential pair exposed to any
 * authenticated caller of this endpoint. This is unrelated to the presigned-URL/
 * third-party-API-key findings elsewhere in this suite (those were scoped/time-limited);
 * this is a full AWS IAM credential leak from the application's own server environment.
 * SanitizedApiLoggingFilter's key/token/secret/password field-name mask already covers
 * both field names ("...ACCESS_KEY_ID" and "...SECRET_ACCESS_KEY" both contain "key"),
 * verified live - but the underlying application bug needs urgent attention from
 * whoever owns this backend/AWS account (credential rotation, and removing "env" from
 * this response). No assertion below reads, asserts on, or logs the "env" field or any
 * value inside it.
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Business Units")
public class BusinessUnitApiTest extends BaseApiTest {

    @Test(groups = {"sanity", "regression", "positive"}, description = "Get All returns the business units list envelope")
    @Story("Get All")
    @Description("Verified live: HTTP 200, data.data/data.meta present. Deliberately does not inspect the " +
            "response's 'env' field - see class Javadoc security finding.")
    public void getAllReturnsBusinessUnitsEnvelope() {
        Response response = given().spec(requestSpecification).get("/web/business-unit/get-all");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertNotNull(response.jsonPath().get("data.data"), "Response should include data.data");
        Assert.assertNotNull(response.jsonPath().get("data.meta"), "Response should include data.meta");
    }

    @Test(groups = {"sanity", "regression", "positive"}, description = "Get Business Unit by ID returns the business unit's details")
    @Story("Get Business Unit by ID")
    @Description("Verified live: HTTP 200 for id=1 (the logged-in user's own business unit), name/uuid present.")
    public void getBusinessUnitByIdReturnsDetails() {
        Response response = given().spec(requestSpecification).get("/web/business-unit/1");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertNotNull(response.jsonPath().getString("data.name"));
        Assert.assertNotNull(response.jsonPath().getString("data.uuid"));
    }

    @Test(groups = { "regression", "negative"}, description = "Get Business Unit by ID without authentication fails")
    @Story("Get Business Unit by ID")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getBusinessUnitByIdWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/business-unit/1");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
