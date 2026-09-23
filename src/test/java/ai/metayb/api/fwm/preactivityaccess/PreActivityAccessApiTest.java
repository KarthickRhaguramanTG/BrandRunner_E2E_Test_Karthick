package ai.metayb.api.fwm.preactivityaccess;

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
 * "03. FWM / Pre-Activity Access" - the one read request: Get Activity Access by
 * Designation. The other 5 requests in this folder (Toggle Initiation Form x2,
 * Update Mobile Permissions x3) are all POST and blocked - see
 * PreActivityAccessWriteOperationsApiTest.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Pre-Activity Access")
public class PreActivityAccessApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Activity Access by Designation returns per-activity permission grids")
    @Story("Get Activity Access by Designation")
    @Description("Verified live: HTTP 200, data is a non-empty array of {initiationStatus, activityId, activityName, permissions}.")
    public void getActivityAccessByDesignationReturnsGrid() {
        Response response = given().spec(requestSpecification).get("/web/pre-activity/getStandardActivityWithDesignationAccess");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = { "regression", "negative"}, description = "Get Activity Access by Designation without authentication fails")
    @Story("Get Activity Access by Designation")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getActivityAccessByDesignationWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/pre-activity/getStandardActivityWithDesignationAccess");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
