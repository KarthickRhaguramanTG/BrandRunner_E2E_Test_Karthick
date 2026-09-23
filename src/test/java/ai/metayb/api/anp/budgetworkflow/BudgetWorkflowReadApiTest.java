package ai.metayb.api.anp.budgetworkflow;

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
 * "02. ANP / Budget Workflow" - read requests: Get Workflow Approval Targets by
 * Budget ID, Get Workflow by Budget ID, Preview Budget Approval, Get Workflow
 * Comments by Budget ID, Get Workflow Comment by Budget ID, Get Approval Screen by
 * Budget ID.
 *
 * budgetId=18 and workflowId=33 are real, pre-existing records in this QA tenant
 * (workflowId discovered from Get Workflow by Budget ID's own response), not fabricated.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Budget Workflow")
public class BudgetWorkflowReadApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Workflow Approval Targets by Budget ID returns the approval eligibility for a real budget")
    @Story("Get Workflow Approval Targets by Budget ID")
    @Description("Verified live: HTTP 200 for budgetId=18, data.budgetId matches, data.eligible/data.reason present.")
    public void getWorkflowApprovalTargetsReturnsEligibility() {
        Response response = given().spec(requestSpecification).get("/web/budget-workflow/18/workflow/approval-targets");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.budgetId"), 18);
        Assert.assertNotNull(response.jsonPath().get("data.eligible"));
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Workflow by Budget ID returns the workflow history for a real budget")
    @Story("Get Workflow by Budget ID")
    @Description("Verified live: HTTP 200 (page=1, limit=20) for budgetId=18, data is a non-empty array with a real workflowId (33).")
    public void getWorkflowByBudgetIdReturnsHistory() {
        Response response = given().spec(requestSpecification).get("/web/budget-workflow/18/workflow?page=1&limit=20");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Preview Budget Approval returns the approval preview for a real budget")
    @Story("Preview Budget Approval")
    @Description("Verified live: HTTP 200 for budgetId=18, data.header.budgetId matches.")
    public void previewBudgetApprovalReturnsHeader() {
        Response response = given().spec(requestSpecification).get("/web/budget-workflow/budgetApprovalPreview/18");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.header.budgetId"), 18);
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Workflow Comments by Budget ID returns comments for a real workflow")
    @Story("Get Workflow Comments by Budget ID")
    @Description("Verified live: HTTP 200 for budgetId=18/workflowId=33, data is an array (empty - no comments yet, asserted as observed).")
    public void getWorkflowCommentsByBudgetIdReturnsArray() {
        Response response = given().spec(requestSpecification).get("/web/budget-workflow/18/workflow/33/comments");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getList("data"), "data should be a list (may be empty)");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Workflow Comment by Budget ID returns comments for a real budget")
    @Story("Get Workflow Comment by Budget ID")
    @Description("Verified live: this GET actually returns HTTP 201 (not 200) with data as an empty array - " +
            "an unusual status code for a read endpoint, but the real, observed behavior in this environment. " +
            "Documented as-is rather than assuming 200.")
    public void getWorkflowCommentByBudgetIdReturnsArray() {
        Response response = given().spec(requestSpecification).get("/web/budget-workflow/18/workflow/comment");

        Assert.assertEquals(response.statusCode(), 201);
        Assert.assertNotNull(response.jsonPath().getList("data"), "data should be a list (may be empty)");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Approval Screen by Budget ID returns the approval screen data for a real budget")
    @Story("Get Approval Screen by Budget ID")
    @Description("Verified live: HTTP 200 for budgetId=18, data.budget.budgetId matches.")
    public void getApprovalScreenByBudgetIdReturnsData() {
        Response response = given().spec(requestSpecification).get("/web/budget-workflow/18/approval-screen");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.budget.budgetId"), 18);
    }

    @Test(groups = {"regression", "negative"}, description = "Get Workflow by Budget ID without authentication fails")
    @Story("Get Workflow by Budget ID")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getWorkflowByBudgetIdWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/budget-workflow/18/workflow?page=1&limit=20");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
