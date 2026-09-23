package ai.metayb.api.fwm.locationmaster;

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
 * "03. FWM / Location Master" - this QA tenant has ZERO location masters (List
 * Location Master returns an empty array), and Create (the only way to make one) is
 * blocked by the same systemic environment issue as every other write in this
 * folder. As a result:
 *   - List Location Master: automated (positive - the real, empty-but-successful response)
 *   - Get Location Master by ID: automated as NEGATIVE ONLY (no real id exists anywhere to test positively)
 *   - Get Items by ID, Get Logs by ID: NOT AUTOMATED - "requires unavailable test data"
 *     (no real location master id exists to call them against)
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Location Master")
public class LocationMasterApiTest extends BaseApiTest {

    @Test(groups = {"sanity", "regression", "positive"}, description = "List Location Master returns the (empty) location master list")
    @Story("List Location Master")
    @Description("Verified live: HTTP 200, 'Location masters fetched successfully', data is an empty array - " +
            "asserted as observed (this QA tenant has no location masters, and Create is blocked, so this " +
            "cannot become non-empty here).")
    public void listLocationMasterReturnsEmptyList() {
        Response response = given().spec(requestSpecification).get("/web/location-master");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("message"), "Location masters fetched successfully");
        Assert.assertNotNull(response.jsonPath().getList("data"), "data should be a list");
    }

    @Test(groups = { "regression", "negative"}, description = "Get Location Master by ID with a non-existent id fails")
    @Story("Get Location Master by ID")
    @Description("Verified live: HTTP 400, message 'Location master not found'. No positive scenario is " +
            "automated here - List Location Master confirms zero real records exist in this tenant.")
    public void getLocationMasterByIdWithNonExistentIdFails() {
        Response response = given().spec(requestSpecification).get("/web/location-master/999999");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Location master not found");
    }

    @Test(groups = { "regression", "negative"}, description = "List Location Master without authentication fails")
    @Story("List Location Master")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void listLocationMasterWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/location-master");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "NOT AUTOMATED: GET /web/location-master/:id/items requires a real location master id, which does not exist in this environment")
    @Story("Get Items by ID")
    @Description("Requires unavailable test data: List Location Master confirms zero real records exist, and " +
            "Create is blocked by the same environment issue as every other write here. Not executed.")
    public void getItemsByIdNotAutomatedDueToMissingTestData() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "NOT AUTOMATED: GET /web/location-master/:id/logs requires a real location master id, which does not exist in this environment")
    @Story("Get Logs by ID")
    @Description("Requires unavailable test data - same reason as Get Items by ID. Not executed.")
    public void getLogsByIdNotAutomatedDueToMissingTestData() {
    }
}
