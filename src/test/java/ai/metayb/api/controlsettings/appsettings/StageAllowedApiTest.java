package ai.metayb.api.controlsettings.appsettings;

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
 * GET /web/settings/stage-allowed/{stage}/{budgetId} -
 * "01. Control Settings / App Settings / Get Stage Allowed by Budget ID".
 *
 * Path param values come from the collection's own variables: stage=APPROVED, budgetId=1.
 *
 * NOTE on negative path-parameter scenarios: verified live that an invalid stage value
 * ("NOT_A_STAGE"), a non-existent budgetId (9999999), and a non-numeric budgetId ("abc")
 * all return the SAME HTTP 200 {"isAllowed": false} as a normal "not allowed" result -
 * there is no distinguishable error response for bad path params on this endpoint. Per
 * Step 8, this is documented as observed behavior rather than asserted as if it were a
 * validation error, since it demonstrably isn't one.
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - App Settings")
public class StageAllowedApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "Get stage allowed returns a boolean isAllowed flag")
    @Story("Stage Allowed")
    @Description("Verified live: HTTP 200, data.isAllowed present as a boolean, for stage=APPROVED, budgetId=1 " +
            "(the collection's own default variable values).")
    public void getStageAllowedReturnsBooleanFlag() {
        Response response = given().spec(requestSpecification).get("/web/settings/stage-allowed/APPROVED/1");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertNotNull(response.jsonPath().get("data.isAllowed"), "Response should include data.isAllowed");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get stage allowed without authentication fails")
    @Story("Stage Allowed")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getStageAllowedWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/settings/stage-allowed/APPROVED/1");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
