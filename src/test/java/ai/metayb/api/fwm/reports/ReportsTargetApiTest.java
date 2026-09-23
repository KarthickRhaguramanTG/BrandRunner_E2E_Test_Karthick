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
 * "03. FWM / Reports - Target" - all 5 Postman requests, all GET.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Reports - Target")
public class ReportsTargetApiTest extends BaseApiTest {

    private static final String TODAY = java.time.LocalDate.now().toString();

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Filters returns the target report's filter options")
    @Story("Get Filters")
    @Description("Verified live: HTTP 200, data.designations is a non-empty array.")
    public void getFiltersReturnsOptions() {
        Response response = given().spec(requestSpecification)
                .get("/web/target-report/filters?startDate=2026-09-01&endDate=" + TODAY);

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.designations").isEmpty(), "data.designations should be non-empty");
    }

    @Test(groups = {"sanity", "regression"}, description = "Get Workflow Listing, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Get Workflow Listing")
    @Description("Verified live: HTTP 500, message lists 'activityId is required' - Postman's saved request has no params, documented per Step 8.")
    public void getWorkflowListingAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/target-report/workflow-listing");

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertTrue(response.jsonPath().getString("message").contains("activityId is required"));
    }

    @Test(groups = { "regression"}, description = "Get Targets, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Get Targets")
    @Description("Verified live: HTTP 500, message 'Provide workflowId or workflowIds'.")
    public void getTargetsAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/target-report/targets");

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertTrue(response.jsonPath().getString("message").contains("Provide workflowId or workflowIds"));
    }

    @Test(groups = {"sanity", "regression"}, description = "Get User Listing, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Get User Listing")
    @Description("Verified live: HTTP 500, message lists 'fromDate' as missing/invalid - Postman's saved request has no params, documented per Step 8.")
    public void getUserListingAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/target-report/user-listing");

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertTrue(response.jsonPath().getString("message").contains("fromDate"));
    }

    @Test(groups = {"sanity", "regression"}, description = "Get Report Details, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Get Report Details")
    @Description("Verified live: HTTP 500, message lists 'fromDate' as missing/invalid - same gap as Get User Listing.")
    public void getReportDetailsAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification)
                .get("/web/target-report/report-details?startDate=2026-09-01&endDate=" + TODAY);

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertTrue(response.jsonPath().getString("message").contains("fromDate"));
    }

    @Test(groups = { "regression", "negative"}, description = "Get Filters without authentication fails")
    @Story("Get Filters")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getFiltersWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification)
                .get("/web/target-report/filters?startDate=2026-09-01&endDate=" + TODAY);

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
