package ai.metayb.api.fwm.fieldactivity;

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
 * "03. FWM / Field Activity" - standalone lookup requests: Get Location By Filter,
 * Get Users, Get Locations (fully automated); Get Distributor By Product, Get
 * Overdue Assignment Due Dates, Get Route Plan Progress, Get Dynamic Report
 * Designation, Get Forms By Workflow, Get Report Details, Get Supervisor Reconcile
 * Details by ID, Get Promoter Close Sale Details by ID (documented observed
 * behavior - see Description on each, same "incomplete Postman example" pattern
 * already established in Control Settings/ANP).
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Field Activity")
public class FieldActivityLookupApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Location By Filter returns the location list")
    @Story("Get Location By Filter")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, name, parentId, hierarchyId}.")
    public void getLocationByFilterReturnsLocations() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/getLocationByFilter");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Users returns the user list")
    @Story("Get Users")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, name, email, designationId}.")
    public void getUsersReturnsUserList() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/users");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Locations returns the location list")
    @Story("Get Locations")
    @Description("Verified live: HTTP 200, data is a non-empty array (same shape as Get Location By Filter).")
    public void getLocationsReturnsLocations() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/locations");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = { "regression"}, description = "Get Distributor By Product, called exactly as the Postman collection defines it (no query params), returns a validation error")
    @Story("Get Distributor By Product")
    @Description("Verified live: HTTP 400, message 'Invalid input: expected string, received undefined'. " +
            "Postman's saved request has no query string - documented per Step 8 rather than inventing one.")
    public void getDistributorByProductAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/distributor-by-product");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertTrue(response.jsonPath().getString("message").contains("expected string"));
    }

    @Test(groups = { "regression"}, description = "Get Overdue Assignment Due Dates, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Get Overdue Assignment Due Dates")
    @Description("Verified live: HTTP 400, message 'userIds is required'.")
    public void getOverdueAssignmentDueDatesAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/overdue-assignment-due-dates");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "userIds is required");
    }

    @Test(groups = { "regression"}, description = "Get Route Plan Progress, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Get Route Plan Progress")
    @Description("Verified live: HTTP 400, message 'Invalid or missing monthwiseMappingId'.")
    public void getRoutePlanProgressAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/route-plan-progress");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid or missing monthwiseMappingId");
    }

    @Test(groups = { "regression"}, description = "Get Dynamic Report Designation, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Get Dynamic Report Designation")
    @Description("Verified live: HTTP 400, message 'Invalid input: expected string, received undefined'.")
    public void getDynamicReportDesignationAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/get_dynamic_report_designation");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertTrue(response.jsonPath().getString("message").contains("expected string"));
    }

    @Test(groups = { "regression"}, description = "Get Forms By Workflow, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Get Forms By Workflow")
    @Description("Verified live: HTTP 400, message 'Budget undefined not found or not mapped to a standard activity'.")
    public void getFormsByWorkflowAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/get_forms_by_workflow");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertTrue(response.jsonPath().getString("message").contains("not found or not mapped"));
    }

    @Test(groups = {"sanity", "regression"}, description = "Get Report Details, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Get Report Details")
    @Description("Verified live: HTTP 400, message 'budget_id is required'.")
    public void getReportDetailsAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/get_report_details");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "budget_id is required");
    }

    @Test(groups = {"sanity", "regression"}, description = "Get Supervisor Reconcile Details by ID, called with only the real activity id, returns a validation error")
    @Story("Get Supervisor Reconcile Details by ID")
    @Description("Verified live: HTTP 400, message lists 3 missing/invalid fields (Postman's saved request for " +
            "this one has no extra query params either) - documented per Step 8 rather than inventing them.")
    public void getSupervisorReconcileDetailsAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/16/supervisor-reconcile-details");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertTrue(response.jsonPath().getString("message").contains("Invalid input"));
    }

    @Test(groups = {"sanity", "regression"}, description = "Get Promoter Close Sale Details by ID, called with only the real activity id, returns a validation error")
    @Story("Get Promoter Close Sale Details by ID")
    @Description("Verified live: HTTP 400, message lists 2 missing/invalid fields - same gap as Get Supervisor Reconcile Details.")
    public void getPromoterCloseSaleDetailsAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/16/promoter-close-sale-details");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertTrue(response.jsonPath().getString("message").contains("Invalid input"));
    }

    @Test(groups = { "regression", "negative"}, description = "Get Users without authentication fails")
    @Story("Get Users")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getUsersWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/field-activity/users");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
