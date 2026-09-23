package ai.metayb.api.anp.activities;

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
 * "02. ANP / Activities" - read requests: Get Activity List All, Get Activity List,
 * Get Designation Location by ID.
 *
 * activityId=16 is a real, pre-existing activity in this QA tenant (discovered via
 * Get Activity List All), not fabricated.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Activities")
public class ActivityReadApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Activity List All returns every activity")
    @Story("Get Activity List All")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, name, category, status, mode}.")
    public void getActivityListAllReturnsActivities() {
        Response response = given().spec(requestSpecification).get("/web/activity/activity-list/all");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Activity List returns a paginated activity list")
    @Story("Get Activity List")
    @Description("Verified live: HTTP 200 (page=1, limit=20), data.data is a non-empty array.")
    public void getActivityListReturnsPaginatedActivities() {
        Response response = given().spec(requestSpecification).get("/web/activity/activity-list?page=1&limit=20");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertFalse(response.jsonPath().getList("data.data").isEmpty(), "data.data should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Designation Location by ID responds successfully for a real activity id")
    @Story("Get Designation Location by ID")
    @Description("Verified live: HTTP 200 for activityId=16 - data is null for this id (no designation-location " +
            "mapping exists for it), asserted as observed rather than assuming non-null content.")
    public void getDesignationLocationByIdRespondsSuccessfully() {
        Response response = given().spec(requestSpecification).get("/web/activity/designation-location/16");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
    }

    @Test(groups = {"regression", "negative"}, description = "Get Activity List All without authentication fails")
    @Story("Get Activity List All")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getActivityListAllWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/activity/activity-list/all");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
