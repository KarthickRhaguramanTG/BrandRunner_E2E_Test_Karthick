package ai.metayb.api.fwm.continuousstandardactivities;

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
 * "03. FWM / Continuous Standard Activities" - read requests: List Continuous
 * Standard Activities, Get Workflow Entity Filters by ID, Get Assignments by ID,
 * Get Workflow Check by ID (fully automated); Get Schedule Assignments, Assign
 * Users, Get Submissions by ID (documented observed behavior - same "incomplete
 * Postman example" pattern established elsewhere).
 *
 * standardActivityId=432 is a real, pre-existing standard activity in this QA
 * tenant, not fabricated. "Get Submissions by ID / :submissionId" is NOT
 * automated at all - no real submissionId is discoverable anywhere in this
 * environment (see class Javadoc on the write-operations sibling class for why).
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Continuous Standard Activities")
public class ContinuousStandardActivitiesApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "List Continuous Standard Activities returns existing continuous activities")
    @Story("List Continuous Standard Activities")
    @Description("Verified live: HTTP 200, data is a non-empty array of {standardActivityId, workflowsCount, usersCount}.")
    public void listContinuousStandardActivitiesReturnsActivities() {
        Response response = given().spec(requestSpecification).get("/web/continuous-standard-activities");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Workflow Entity Filters by ID returns available workflows for a real standard activity")
    @Story("Get Workflow Entity Filters by ID")
    @Description("Verified live: HTTP 200 for id=432, data.workflows is a non-empty array.")
    public void getWorkflowEntityFiltersByIdReturnsWorkflows() {
        Response response = given().spec(requestSpecification).get("/web/continuous-standard-activities/432/workflow-entity-filters");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.workflows").isEmpty(), "data.workflows should be non-empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Assignments by ID returns real user assignments for a real standard activity")
    @Story("Get Assignments by ID")
    @Description("Verified live: HTTP 200 for id=432, data is a non-empty array of real {id, configGroupId, userId, userName}.")
    public void getAssignmentsByIdReturnsAssignments() {
        Response response = given().spec(requestSpecification).get("/web/continuous-standard-activities/432/assignments");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Workflow Check by ID reports workflow creation status for a real standard activity")
    @Story("Get Workflow Check by ID")
    @Description("Verified live: HTTP 200 for id=432, data.isWorkflowCreated present (true - asserted as observed).")
    public void getWorkflowCheckByIdReturnsStatus() {
        Response response = given().spec(requestSpecification).get("/web/continuous-standard-activities/432/workflow-check");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("data.isWorkflowCreated"), true);
    }

    @Test(groups = {"api", "regression"}, description = "Get Schedule Assignments, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Get Schedule Assignments")
    @Description("Verified live: HTTP 400, message describes 3 missing/invalid inputs - Postman's saved request has no query params, documented per Step 8.")
    public void getScheduleAssignmentsAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/continuous-standard-activities/schedule-assignments");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertTrue(response.jsonPath().getString("message").contains("Invalid input"));
    }

    @Test(groups = {"api", "regression"}, description = "Assign Users, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Assign Users")
    @Description("Verified live: HTTP 400, message 'Invalid input' - same gap as Get Schedule Assignments. " +
            "Note this Postman request is a GET despite its name suggesting a mutation.")
    public void assignUsersAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/continuous-standard-activities/assign-users");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid input");
    }

    @Test(groups = {"api", "regression"}, description = "Get Submissions by ID, called with only the real standard activity id, returns a validation error")
    @Story("Get Submissions by ID")
    @Description("Verified live: HTTP 400, message 'Invalid input: expected number, received NaN' - this " +
            "endpoint needs additional query params Postman's saved request doesn't specify, documented per Step 8.")
    public void getSubmissionsByIdAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/continuous-standard-activities/432/submissions");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertTrue(response.jsonPath().getString("message").contains("NaN"));
    }

    @Test(groups = {"api", "regression", "negative"}, description = "List Continuous Standard Activities without authentication fails")
    @Story("List Continuous Standard Activities")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void listContinuousStandardActivitiesWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/continuous-standard-activities");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "NOT AUTOMATED: GET .../submissions/:submissionId requires a real submissionId that could not be discovered anywhere in this environment")
    @Story("Get Submissions by ID (with submissionId)")
    @Description("Requires unavailable test data: the base Get Submissions by ID call (see above) itself " +
            "fails validation without further parameters this environment's Postman example doesn't supply, " +
            "so no submission list was ever obtained to source a real submissionId from. Not executed.")
    public void getSubmissionsBySubmissionIdNotAutomatedDueToMissingTestData() {
    }
}
