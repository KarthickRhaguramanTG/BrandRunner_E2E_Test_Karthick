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
 * "03. FWM / Reports - Attendance" - both Postman requests.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Reports - Attendance")
public class ReportsAttendanceApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Attendance Report returns the attendance report for a date range")
    @Story("Get Attendance Report")
    @Description("Verified live: HTTP 200 (page=1, limit=10, startDate=2026-09-01, endDate=today), data is a list (empty - asserted as observed).")
    public void getAttendanceReportReturnsData() {
        String today = java.time.LocalDate.now().toString();
        Response response = given().spec(requestSpecification)
                .get("/web/user-attendance-report/getattendanceReports?page=1&limit=10&startDate=2026-09-01&endDate=" + today);

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getList("data"), "data should be a list");
    }

    @Test(groups = { "regression", "negative"}, description = "Get Attendance Report without authentication fails")
    @Story("Get Attendance Report")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getAttendanceReportWithoutAuthFails() {
        String today = java.time.LocalDate.now().toString();
        Response response = given().spec(noAuthRequestSpecification)
                .get("/web/user-attendance-report/getattendanceReports?page=1&limit=10&startDate=2026-09-01&endDate=" + today);

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(enabled = false, groups = { "regression"}, description = "BLOCKED: POST /web/user-attendance-report/getIndividualattendanceDetails returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Get Individual Attendance")
    @Description("Blocked by the same systemic CloudFront/S3 SPA-fallback root cause confirmed throughout this folder. Directly verified live. Not executed.")
    public void getIndividualAttendanceNotAutomatedDueToEnvironmentIssue() {
    }
}
