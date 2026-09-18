package ai.metayb.api.fwm.workflowmanagement;

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
 * "03. FWM / Workflow Management" - read requests: List Workflow Management, Get
 * Workflow Management by ID, Get Versions by ID, Get Versions Standard Activity,
 * Get AI Projects (fully automated); Get AI Models (documented observed behavior).
 *
 * Workflow id=508 (from the list's activeVersion.id) and standardActivityId=432 are
 * real, pre-existing records in this QA tenant, not fabricated.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Workflow Management")
public class WorkflowManagementApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "List Workflow Management returns existing workflows")
    @Story("List Workflow Management")
    @Description("Verified live: HTTP 200 (page=1, limit=20), data.data is a non-empty array of {workflowRefId, standardActivityId, activeVersion}.")
    public void listWorkflowManagementReturnsWorkflows() {
        Response response = given().spec(requestSpecification).get("/web/workflow?page=1&limit=20");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.data").isEmpty(), "data.data should be non-empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Workflow Management by ID returns a real workflow's details")
    @Story("Get Workflow Management by ID")
    @Description("Verified live: HTTP 200 for id=508, data.name/data.workflowStatus present.")
    public void getWorkflowManagementByIdReturnsDetails() {
        Response response = given().spec(requestSpecification).get("/web/workflow/508");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.id"), 508);
        Assert.assertNotNull(response.jsonPath().getString("data.name"));
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Versions by ID returns version history for a real workflow")
    @Story("Get Versions by ID")
    @Description("Verified live: HTTP 200 for id=508, data is a non-empty array.")
    public void getVersionsByIdReturnsHistory() {
        Response response = given().spec(requestSpecification).get("/web/workflow/508/versions");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Versions Standard Activity returns workflows for a real standard activity")
    @Story("Get Versions Standard Activity")
    @Description("Verified live: HTTP 200 for standardActivityId=432, data is a non-empty array.")
    public void getVersionsStandardActivityReturnsWorkflows() {
        Response response = given().spec(requestSpecification).get("/web/workflow/versions/standard-activity/432");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get AI Projects returns the AI project list")
    @Story("Get AI Projects")
    @Description("Verified live: HTTP 200, 'AI projects fetched successfully', data.projects present (empty - asserted as observed).")
    public void getAiProjectsReturnsList() {
        Response response = given().spec(requestSpecification).get("/web/workflow/ai-projects");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getList("data.projects"), "data.projects should be a list");
    }

    @Test(groups = {"api", "regression"}, description = "Get AI Models, called exactly as the Postman collection defines it (no query params), returns a validation error")
    @Story("Get AI Models")
    @Description("Verified live: HTTP 400, message 'project_id is required, task_type must be a valid AI task type'. " +
            "Postman's saved request has no query string - documented per Step 8 rather than inventing one.")
    public void getAiModelsAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/workflow/ai-models");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "project_id is required, task_type must be a valid AI task type");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "List Workflow Management without authentication fails")
    @Story("List Workflow Management")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void listWorkflowManagementWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/workflow?page=1&limit=20");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
