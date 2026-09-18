package ai.metayb.api.fwm.reports;

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
 * "03. FWM / Reports - Standard Activity" - all 5 Postman requests.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Reports - Standard Activity")
public class ReportsStandardActivityApiTest extends BaseApiTest {

    private static final String TODAY = java.time.LocalDate.now().toString();

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Filters returns the report's filter options")
    @Story("Get Filters")
    @Description("Verified live: HTTP 200, data.designations is a non-empty array.")
    public void getFiltersReturnsOptions() {
        Response response = given().spec(requestSpecification)
                .get("/web/standard-activity-report/filters?startDate=2026-09-01&endDate=" + TODAY);

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.designations").isEmpty(), "data.designations should be non-empty");
    }

    @Test(groups = {"api", "regression"}, description = "Get User Listing, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Get User Listing")
    @Description("Verified live: HTTP 500, message lists 'activityId is required' - Postman's saved request has " +
            "no activityId param, documented per Step 8 rather than inventing one.")
    public void getUserListingAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification)
                .get("/web/standard-activity-report/user-listing?startDate=2026-09-01&endDate=" + TODAY);

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertTrue(response.jsonPath().getString("message").contains("activityId is required"));
    }

    @Test(groups = {"api", "regression"}, description = "Get Report Details, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Get Report Details")
    @Description("Verified live: HTTP 500, message lists 'activityId is required' - same gap as Get User Listing.")
    public void getReportDetailsAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification)
                .get("/web/standard-activity-report/report-details?startDate=2026-09-01&endDate=" + TODAY + "&page=1&limit=20");

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertTrue(response.jsonPath().getString("message").contains("activityId is required"));
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get Submissions by ID with a non-existent submission fails")
    @Story("Get Submissions by ID")
    @Description("Verified live: HTTP 400, message 'Activity form submission not found' for id=432 (a real " +
            "standard activity id, but not a real submission id - no positive scenario is automated since no " +
            "real form submission exists anywhere reachable in this environment).")
    public void getSubmissionsByIdWithNonExistentSubmissionFails() {
        Response response = given().spec(requestSpecification).get("/web/standard-activity-report/submissions/432");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Activity form submission not found");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get Filters without authentication fails")
    @Story("Get Filters")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getFiltersWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification)
                .get("/web/standard-activity-report/filters?startDate=2026-09-01&endDate=" + TODAY);

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: PUT /web/standard-activity-report/submissions/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Submissions by ID")
    @Description("Blocked by the same systemic CloudFront/S3 SPA-fallback root cause confirmed throughout this folder. Not executed.")
    public void updateSubmissionsByIdNotAutomatedDueToEnvironmentIssue() {
    }
}
