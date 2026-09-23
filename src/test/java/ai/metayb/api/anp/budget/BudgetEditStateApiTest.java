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
 * "02. ANP / Budget" - requests whose real, current behavior depends on budget state
 * or on an id this environment doesn't expose anywhere: Get Pre Update Form Fields by
 * Budget ID, Get Pre Update Activity by Budget ID, Get Edit Bulk by Budget Group ID.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Budget")
public class BudgetEditStateApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Pre Update Form Fields by Budget ID returns form data for a real budget")
    @Story("Get Pre Update Form Fields by Budget ID")
    @Description("Verified live: HTTP 200 for budgetId=18, data.budgetInfo present.")
    public void getPreUpdateFormFieldsReturnsData() {
        Response response = given().spec(requestSpecification).get("/web/budget/18/pre-update-form-fields");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().get("data.budgetInfo"), "data.budgetInfo should be present");
    }

    @Test(groups = {"sanity", "regression"}, description = "Get Pre Update Activity by Budget ID is rejected while the budget is not in the required state")
    @Story("Get Pre Update Activity by Budget ID")
    @Description("Verified live: HTTP 400, message 'Pre-update activity is only accessible when status is " +
            "'pre_update_activity_pending'. Current: pre_activity_rejected' - budgetId=18's real current status " +
            "in this QA tenant. This is the actual, observed behavior for the only real budget id available; " +
            "reaching the true positive state would require driving a budget through a specific workflow " +
            "transition, which needs the write endpoints this environment currently blocks (see " +
            "BudgetWorkflowWriteOperationsApiTest). Documented per Step 8 rather than invented.")
    public void getPreUpdateActivityIsRejectedInCurrentBudgetState() {
        Response response = given().spec(requestSpecification).get("/web/budget/18/pre-update-activity");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertTrue(response.jsonPath().getString("message").contains("pre_update_activity_pending"));
    }

    @Test(groups = { "regression", "negative"}, description = "Get Edit Bulk by Budget Group ID with a non-existent group id fails")
    @Story("Get Edit Bulk by Budget Group ID")
    @Description("Verified live: HTTP 500, message 'No budgets found for this group'. No API in this collection " +
            "exposes a real budgetGroupId value (Get Budget List's items carry no group field), so no positive " +
            "scenario could be constructed without inventing an id - documented as negative-only per Step 8.")
    public void getEditBulkByBudgetGroupIdWithNonExistentIdFails() {
        Response response = given().spec(requestSpecification).get("/web/budget/edit/bulk/999999");

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("message"), "No budgets found for this group");
    }
}
