package ai.metayb.api.anp.budgetactuals;

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
 * "02. ANP / Budget Actuals" - read requests: Get Budget Actuals (automated), Get
 * Actuals Detail (NOT automated).
 *
 * "Get Actuals Detail" (GET /web/budget-actuals/:budgetId/actuals/:actualMasterId)
 * requires a real actualMasterId. Get Budget Actuals confirms budgetId=18 has zero
 * actuals ({"actuals":[]}), and Submit Budget Actuals (the only way to create one) is
 * blocked (see BudgetActualsWriteOperationsApiTest) - no real actualMasterId exists
 * anywhere reachable in this environment. Marked "requires unavailable test data" per
 * Section 3 rather than inventing an id.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Budget Actuals")
public class BudgetActualsApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Budget Actuals returns the actuals status for a real budget")
    @Story("Get Budget Actuals")
    @Description("Verified live: HTTP 200 for budgetId=18, data.enableActuals present, data.actuals is an array (empty for this budget).")
    public void getBudgetActualsReturnsStatus() {
        Response response = given().spec(requestSpecification).get("/web/budget-actuals/18/actuals");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().get("data.enableActuals"));
        Assert.assertNotNull(response.jsonPath().getList("data.actuals"), "data.actuals should be a list (may be empty)");
    }

    @Test(groups = {"sanity", "regression", "negative"}, description = "Get Budget Actuals without authentication fails")
    @Story("Get Budget Actuals")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getBudgetActualsWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/budget-actuals/18/actuals");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "NOT AUTOMATED: GET .../actuals/:actualMasterId requires a real actualMasterId that does not exist in this environment")
    @Story("Get Actuals Detail")
    @Description("Requires unavailable test data: Get Budget Actuals confirms budgetId=18 has zero actuals, " +
            "and Submit Budget Actuals (the only way to create one) is blocked by the same environment issue " +
            "(see BudgetActualsWriteOperationsApiTest). See class Javadoc. Not executed.")
    public void getActualsDetailNotAutomatedDueToMissingTestData() {
    }
}
