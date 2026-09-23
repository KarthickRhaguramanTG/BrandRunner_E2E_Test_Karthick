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
 * "03. FWM / Field Activity" - core per-activity read requests: Get Field Activity by
 * ID, Get Users by ID, Get Individual Users, Assign by ID, Get Workflow Check by ID,
 * Get Workflow Entity Filters by ID, Get Attendance Summary by ID, Get
 * Formeventscount by ID, Get Allocationperproduct by ID.
 *
 * id=16 is the same real, pre-existing activity used throughout "02. ANP" (this
 * endpoint namespace shares the same underlying budget/activity records), not fabricated.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Field Activity")
public class FieldActivityCoreApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Field Activity by ID returns a real activity's details")
    @Story("Get Field Activity by ID")
    @Description("Verified live: HTTP 200 for id=16, data.title/data.status present.")
    public void getFieldActivityByIdReturnsDetails() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/16");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getString("data.title"));
        Assert.assertNotNull(response.jsonPath().getString("data.status"));
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Users by ID returns field users and team leads for a real activity")
    @Story("Get Users by ID")
    @Description("Verified live: HTTP 200 for id=16, data.fieldUsers/data.teamLeads present (empty arrays - asserted as observed).")
    public void getUsersByIdReturnsUserLists() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/16/users");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getList("data.fieldUsers"), "data.fieldUsers should be a list");
        Assert.assertNotNull(response.jsonPath().getList("data.teamLeads"), "data.teamLeads should be a list");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Individual Users returns individually-assignable users for a real activity")
    @Story("Get Individual Users")
    @Description("Verified live: HTTP 200 for id=16, data.users present.")
    public void getIndividualUsersReturnsUsers() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/16/individual-users");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getList("data.users"), "data.users should be a list");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Assign by ID returns the current assignment state for a real activity")
    @Story("Assign by ID")
    @Description("Verified live: HTTP 200 for id=16, data.budgetId matches, data.totalAssignments present.")
    public void assignByIdReturnsAssignmentState() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/16/assign");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.budgetId"), 16);
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Workflow Check by ID reports workflow creation status for a real activity")
    @Story("Get Workflow Check by ID")
    @Description("Verified live: HTTP 200 for id=16, data.isWorkflowCreated present.")
    public void getWorkflowCheckByIdReturnsStatus() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/16/workflow-check");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().get("data.isWorkflowCreated"));
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Workflow Entity Filters by ID returns available workflows for a real activity")
    @Story("Get Workflow Entity Filters by ID")
    @Description("Verified live: HTTP 200 for id=16, data.workflows is a non-empty array.")
    public void getWorkflowEntityFiltersByIdReturnsWorkflows() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/16/workflow-entity-filters");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.workflows").isEmpty(), "data.workflows should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Attendance Summary by ID returns attendance summary for a real activity")
    @Story("Get Attendance Summary by ID")
    @Description("Verified live: HTTP 200 for id=16, data.data/data.tableData/data.dates present (empty - asserted as observed).")
    public void getAttendanceSummaryByIdReturnsSummary() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/16/attendance_summary");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getList("data.data"), "data.data should be a list");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Formeventscount by ID returns a form-events count for a real activity")
    @Story("Get Formeventscount by ID")
    @Description("Verified live: HTTP 200 for id=16, data.count present.")
    public void getFormeventscountByIdReturnsCount() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/formeventscount/16");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getString("data.count"));
    }

    @Test(groups = {"regressison", "sanity", "positive"}, description = "Get Allocationperproduct by ID returns per-product allocations for a real activity")
    @Story("Get Allocationperproduct by ID")
    @Description("Verified live: HTTP 200 for id=16, data is a list (empty - asserted as observed).")
    public void getAllocationperproductByIdReturnsList() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/allocationperproduct/16");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getList("data"), "data should be a list");
    }

    @Test(groups = { "regression", "negative"}, description = "Get Field Activity by ID without authentication fails")
    @Story("Get Field Activity by ID")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getFieldActivityByIdWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/field-activity/16");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
