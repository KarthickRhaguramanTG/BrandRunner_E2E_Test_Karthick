package ai.metayb.api.fwm.targetmanagement;

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
 * "03. FWM / Target Management" - read requests: List Target Management, Get
 * Target Management by ID, Get Workflow Options.
 *
 * id=56 ("Target Flow") is a real, pre-existing target in this QA tenant, not fabricated.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Target Management")
public class TargetManagementApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "List Target Management returns existing targets")
    @Story("List Target Management")
    @Description("Verified live: HTTP 200 (page=1, limit=20), data.data is a non-empty array of {id, targetName, workflowId}.")
    public void listTargetManagementReturnsTargets() {
        Response response = given().spec(requestSpecification).get("/web/targetmanagement?page=1&limit=20");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.data").isEmpty(), "data.data should be non-empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Target Management by ID returns a real target's details")
    @Story("Get Target Management by ID")
    @Description("Verified live: HTTP 200 for id=56, data.targetName present.")
    public void getTargetManagementByIdReturnsDetails() {
        Response response = given().spec(requestSpecification).get("/web/targetmanagement/56");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.id"), 56);
        Assert.assertNotNull(response.jsonPath().getString("data.targetName"));
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Workflow Options returns workflows available for targeting")
    @Story("Get Workflow Options")
    @Description("Verified live: HTTP 200 (page=1, limit=20), data.data is a non-empty array.")
    public void getWorkflowOptionsReturnsOptions() {
        Response response = given().spec(requestSpecification).get("/web/targetmanagement/workflow-options?page=1&limit=20");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.data").isEmpty(), "data.data should be non-empty");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "List Target Management without authentication fails")
    @Story("List Target Management")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void listTargetManagementWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/targetmanagement?page=1&limit=20");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
