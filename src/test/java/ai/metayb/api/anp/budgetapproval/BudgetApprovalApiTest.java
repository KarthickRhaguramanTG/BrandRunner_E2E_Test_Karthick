package ai.metayb.api.anp.budgetapproval;

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
 * "02. ANP / Budget Approval" - every request in this sub-area is GET, and 5 of the 6
 * are automated here: Get Approval FWM Details, Get Approval Master Options, Get
 * Approval Activity Media, Get Approval Reference, Get Approval by Budget ID.
 *
 * NOT AUTOMATED: "Download Approval Activity Media by Budget ID" (GET
 * /web/budget-approval/:budgetId/approval/activity-media/:mediaId/download) requires a
 * real mediaId, and Get Approval Activity Media confirms this budget has zero media
 * items ({"items":[],...}) - there is no real media anywhere in this business unit to
 * reference, and Create/Upload endpoints that could produce one are blocked (see
 * BudgetWriteOperationsApiTest, Upload Media). Marked "requires unavailable test data"
 * per Section 3 rather than inventing a mediaId.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Budget Approval")
public class BudgetApprovalApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Approval FWM Details by Budget ID returns FWM integration details for a real budget")
    @Story("Get Approval FWM Details by Budget ID")
    @Description("Verified live: HTTP 200 for budgetId=18, data.isFWMIntegrated present (false for this budget).")
    public void getApprovalFwmDetailsReturnsData() {
        Response response = given().spec(requestSpecification).get("/web/budget-approval/18/approval/fwm-details");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().get("data.isFWMIntegrated"));
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Approval Master Options by Budget ID returns the master option lists for a real budget")
    @Story("Get Approval Master Options by Budget ID")
    @Description("Verified live: HTTP 200 for budgetId=18, data.masterOptions is a non-empty object.")
    public void getApprovalMasterOptionsReturnsOptions() {
        Response response = given().spec(requestSpecification).get("/web/budget-approval/18/approval/master-options");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().get("data.masterOptions"), "data.masterOptions should be present");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Approval Activity Media by Budget ID returns the media listing for a real budget")
    @Story("Get Approval Activity Media by Budget ID")
    @Description("Verified live: HTTP 200 for budgetId=18, data.items is an array (empty for this budget - " +
            "asserted as observed; this is also why the paired Download request cannot be automated - see class Javadoc).")
    public void getApprovalActivityMediaReturnsListing() {
        Response response = given().spec(requestSpecification).get("/web/budget-approval/18/approval/activity-media");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getList("data.items"), "data.items should be a list (may be empty)");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Approval Reference by Budget ID returns reference details for a real budget")
    @Story("Get Approval Reference by Budget ID")
    @Description("Verified live: HTTP 200 for budgetId=18, data.budgetCode present.")
    public void getApprovalReferenceReturnsDetails() {
        Response response = given().spec(requestSpecification).get("/web/budget-approval/18/approval/reference");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getString("data.budgetCode"));
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Approval by Budget ID returns the full approval record for a real budget")
    @Story("Get Approval by Budget ID")
    @Description("Verified live: HTTP 200 for budgetId=18, data.budgetId matches.")
    public void getApprovalByBudgetIdReturnsRecord() {
        Response response = given().spec(requestSpecification).get("/web/budget-approval/18/approval");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.budgetId"), 18);
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get Approval by Budget ID without authentication fails")
    @Story("Get Approval by Budget ID")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getApprovalByBudgetIdWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/budget-approval/18/approval");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "NOT AUTOMATED: GET .../approval/activity-media/:mediaId/download requires a real mediaId that does not exist in this environment")
    @Story("Download Approval Activity Media by Budget ID")
    @Description("Requires unavailable test data: Get Approval Activity Media confirms budgetId=18 has zero " +
            "media items, and the endpoints that could create one (Upload Media, Submit Budget Actuals) are " +
            "blocked by the same environment issue. See class Javadoc. Not executed.")
    public void downloadApprovalActivityMediaNotAutomatedDueToMissingTestData() {
    }
}
