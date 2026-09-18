package ai.metayb.api.fwm.reports;

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
 * "03. FWM / Reports - Performance" - all 4 Postman requests, all GET.
 *
 * All 4 return a validation error when called exactly as Postman defines them (no
 * query params) since they all require a userId - documented per Step 8 rather than
 * inventing one, consistent with the pattern already established for several other
 * report endpoints in this folder.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Reports - Performance")
public class ReportsPerformanceApiTest extends BaseApiTest {

    @Test(groups = {"api", "regression"}, description = "Get Report Details, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Get Report Details")
    @Description("Verified live: HTTP 500, message lists 'userId' as missing/invalid.")
    public void getReportDetailsAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/performance-report/reportDetails");

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertTrue(response.jsonPath().getString("message").contains("userId"));
    }

    @Test(groups = {"api", "regression"}, description = "Get User Details, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Get User Details")
    @Description("Verified live: HTTP 500, message lists 'userId' as missing/invalid - same gap as Get Report Details.")
    public void getUserDetailsAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/performance-report/userDetails");

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertTrue(response.jsonPath().getString("message").contains("userId"));
    }

    @Test(groups = {"api", "regression"}, description = "Get User Attendance Metrics, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Get User Attendance Metrics")
    @Description("Verified live: HTTP 500, message lists 'userId' as missing/invalid - same gap as Get Report Details.")
    public void getUserAttendanceMetricsAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/performance-report/getUserAttendanceMetrics");

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertTrue(response.jsonPath().getString("message").contains("userId"));
    }

    @Test(groups = {"api", "regression"}, description = "Get Total Activities, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Get Total Activities")
    @Description("Verified live: HTTP 500, message lists 'userId' as missing/invalid - same gap as Get Report Details.")
    public void getTotalActivitiesAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/performance-report/totalActivities");

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertTrue(response.jsonPath().getString("message").contains("userId"));
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get Report Details without authentication fails")
    @Story("Get Report Details")
    @Description("Verified live: HTTP 401, message 'Authentication token missing' - the auth check happens before the validation above.")
    public void getReportDetailsWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/performance-report/reportDetails");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
