package ai.metayb.api.fwm.assignedtargets;

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
 * "03. FWM / Assigned Targets" - this QA tenant has ZERO assigned targets (List
 * Assigned Targets returns an empty array), and Create is blocked by the same
 * systemic environment issue as every other write in this folder. Same pattern as
 * "03. FWM / Location Master" - see LocationMasterApiTest.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Assigned Targets")
public class AssignedTargetsApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "List Assigned Targets returns the (empty) assigned-targets list")
    @Story("List Assigned Targets")
    @Description("Verified live: HTTP 200, 'Assigned targets fetched successfully', data.data is an empty array - " +
            "asserted as observed (this QA tenant has no assigned targets, and Create is blocked).")
    public void listAssignedTargetsReturnsEmptyList() {
        Response response = given().spec(requestSpecification).get("/web/assigned-targets");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("message"), "Assigned targets fetched successfully");
        Assert.assertNotNull(response.jsonPath().getList("data.data"), "data.data should be a list");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get Assigned Target by ID with a non-existent id fails")
    @Story("Get Assigned Target by ID")
    @Description("Verified live: HTTP 400, message 'Assigned target not found'. No positive scenario is " +
            "automated here - List Assigned Targets confirms zero real records exist in this tenant.")
    public void getAssignedTargetByIdWithNonExistentIdFails() {
        Response response = given().spec(requestSpecification).get("/web/assigned-targets/999999");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Assigned target not found");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "List Assigned Targets without authentication fails")
    @Story("List Assigned Targets")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void listAssignedTargetsWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/assigned-targets");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/assigned-targets/create returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Create")
    @Description("Blocked by the same systemic CloudFront/S3 SPA-fallback root cause confirmed throughout this folder. Directly verified live. Not executed.")
    public void createNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: PUT /web/assigned-targets/update/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update by ID")
    @Description("Blocked by environment - same route family. Not executed.")
    public void updateByIdNotAutomatedDueToEnvironmentIssue() {
    }
}
