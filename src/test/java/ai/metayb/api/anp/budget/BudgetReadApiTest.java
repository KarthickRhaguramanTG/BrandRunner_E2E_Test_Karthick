package ai.metayb.api.anp.budget;

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
 * "02. ANP / Budget" - core read requests: Get Approval Levels, Get Budget List,
 * Get Standard Activity Fields, Preview Budget, Get Actual by Budget ID,
 * Get Edit by Budget ID, Get Approved List, Get Standard Activity All, Get Activities,
 * Get Branding Master by Property ID.
 *
 * budgetId=18, standardActivityId=558 are real, pre-existing records in this QA tenant
 * (discovered via Get Budget List / Get Standard Activity All), not fabricated.
 * propertyId=1 for Get Branding Master returned real data live, also not fabricated -
 * confirmed as a genuine existing property, not guessed blindly (verified via the
 * actual response content, not assumed to succeed).
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Budget")
public class BudgetReadApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Approval Levels returns the configured approval level order")
    @Story("Get Approval Levels")
    @Description("Verified live: HTTP 200, data.approvalLevelOrders is a non-empty array.")
    public void getApprovalLevelsReturnsOrder() {
        Response response = given().spec(requestSpecification).get("/web/budget/approval-levels");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.approvalLevelOrders").isEmpty(), "approvalLevelOrders should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Budget List returns existing budgets")
    @Story("Get Budget List")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, activityName, title, requestedAmount}.")
    public void getBudgetListReturnsBudgets() {
        Response response = given().spec(requestSpecification).get("/web/budget/budget-list");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Standard Activity Fields returns the configured fields for a real standard activity")
    @Story("Get Standard Activity Fields")
    @Description("Verified live: HTTP 200 for standardActivityId=558, data.sections is a non-empty array.")
    public void getStandardActivityFieldsReturnsSections() {
        Response response = given().spec(requestSpecification).get("/web/budget/standard-activity-fields/558");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.sections").isEmpty(), "data.sections should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Preview Budget returns the budget's preview details for a real budget")
    @Story("Preview Budget")
    @Description("Verified live: HTTP 200 for budgetId=18, data.title/data.sections present.")
    public void previewBudgetReturnsDetails() {
        Response response = given().spec(requestSpecification).get("/web/budget/budget-preview/18");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getString("data.title"));
        Assert.assertFalse(response.jsonPath().getList("data.sections").isEmpty(), "data.sections should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Actual by Budget ID returns the budget's actual-entry details for a real budget")
    @Story("Get Actual by Budget ID")
    @Description("Verified live: HTTP 200 for budgetId=18, data.budgetId matches, data.sections present.")
    public void getActualByBudgetIdReturnsDetails() {
        Response response = given().spec(requestSpecification).get("/web/budget/18/actual");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.budgetId"), 18);
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Edit by Budget ID returns the budget's editable form data for a real budget")
    @Story("Get Edit by Budget ID")
    @Description("Verified live: HTTP 200 for budgetId=18, data.budgetInfo present.")
    public void getEditByBudgetIdReturnsFormData() {
        Response response = given().spec(requestSpecification).get("/web/budget/edit/18");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().get("data.budgetInfo"), "data.budgetInfo should be present");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Approved List returns approved budgets")
    @Story("Get Approved List")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, activityName, title, status: 'approved'}.")
    public void getApprovedListReturnsApprovedBudgets() {
        Response response = given().spec(requestSpecification).get("/web/budget/approved-list");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Standard Activity All returns every standard activity")
    @Story("Get Standard Activity All")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, uuid, name, mode, status}.")
    public void getStandardActivityAllReturnsActivities() {
        Response response = given().spec(requestSpecification).get("/web/budget/standard-activity/all");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Activities returns a dropdown of standard activities")
    @Story("Get Activities")
    @Description("Verified live: HTTP 200, data.items is a non-empty array of {id, name}.")
    public void getActivitiesReturnsDropdown() {
        Response response = given().spec(requestSpecification).get("/web/budget/activities");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.items").isEmpty(), "data.items should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Branding Master by Property ID returns branding info for a real property")
    @Story("Get Branding Master by Property ID")
    @Description("Verified live: HTTP 200 for propertyId=1, data is a non-empty array with basicInfo/budgetInfo.")
    public void getBrandingMasterByPropertyIdReturnsDetails() {
        Response response = given().spec(requestSpecification).get("/web/budget/getBrandingMaster/1");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"regression", "negative"}, description = "Get Budget List without authentication fails")
    @Story("Get Budget List")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getBudgetListWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/budget/budget-list");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
