package ai.metayb.api.masterdata.uoms;

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
 * "05. Master Data / UOMs" - both Postman requests, kept in one class per Section 16
 * ("avoid unnecessary classes for simple APIs").
 */
@Epic("BrandRunners Web API")
@Feature("Master Data - UOMs")
public class UomsApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "List UOMs returns existing units of measure")
    @Story("List UOMs")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, businessUnitId, name, code, isActive}.")
    public void listUomsReturnsUnits() {
        Response response = given().spec(requestSpecification).get("/web/uoms");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = { "regression", "negative"}, description = "List UOMs without authentication fails")
    @Story("List UOMs")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void listUomsWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/uoms");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(enabled = false, groups = { "regression"}, description = "BLOCKED: PUT /web/uoms returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update UOM")
    @Description("Blocked by the same systemic CloudFront/S3 SPA-fallback root cause confirmed throughout every folder automated so far. Directly verified live. Not executed.")
    public void updateUomNotAutomatedDueToEnvironmentIssue() {
    }
}
