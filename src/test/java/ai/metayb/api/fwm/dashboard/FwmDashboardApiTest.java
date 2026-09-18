package ai.metayb.api.fwm.dashboard;

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
 * "03. FWM / Dashboard" - all 7 Postman requests, all GET, all automated:
 * Status Summary, User Attendance, Total Activities, User Login Times, Weekly
 * Activity Trend, Activity Table, Filtered Users by Category.
 *
 * Query params (date/locationId/designationId) mirror the Postman collection's own
 * values exactly, with "today" substituted for the collection's pinned example date
 * (locationId/designationId stay "null" strings, as Postman's own saved requests use).
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Dashboard")
public class FwmDashboardApiTest extends BaseApiTest {

    private static final String TODAY = java.time.LocalDate.now().toString();

    @Test(groups = {"api", "smoke", "positive"}, description = "Status Summary returns today's field-user status counts")
    @Story("Status Summary")
    @Description("Verified live: HTTP 200, data.statusSummary present with day_started_users/active_users/total_activities.")
    public void statusSummaryReturnsCounts() {
        Response response = given().spec(requestSpecification)
                .get("/web/fwm-dashboard/status-summary?date=" + TODAY + "&locationId=null&designationId=null");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().get("data.statusSummary"), "data.statusSummary should be present");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "User Attendance returns attendance data for today")
    @Story("User Attendance")
    @Description("Verified live: HTTP 200.")
    public void userAttendanceReturnsData() {
        Response response = given().spec(requestSpecification)
                .get("/web/fwm-dashboard/user-attendance?date=" + TODAY + "&locationId=null&designationId=null");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Total Activities returns activity counts for today")
    @Story("Total Activities")
    @Description("Verified live: HTTP 200.")
    public void totalActivitiesReturnsCounts() {
        Response response = given().spec(requestSpecification)
                .get("/web/fwm-dashboard/total-activities?date=" + TODAY + "&locationId=null&designationId=null");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "User Login Times returns login-time data for today")
    @Story("User Login Times")
    @Description("Verified live: HTTP 200.")
    public void userLoginTimesReturnsData() {
        Response response = given().spec(requestSpecification)
                .get("/web/fwm-dashboard/user-login-times?date=" + TODAY + "&locationId=null&designationId=null");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Weekly Activity Trend returns trend data")
    @Story("Weekly Activity Trend")
    @Description("Verified live: HTTP 200.")
    public void weeklyActivityTrendReturnsData() {
        Response response = given().spec(requestSpecification)
                .get("/web/fwm-dashboard/weekly-activity-trend?date=" + TODAY + "&locationId=null&designationId=null");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Activity Table returns a paginated activity table")
    @Story("Activity Table")
    @Description("Verified live: HTTP 200 (page=1, pageSize=20).")
    public void activityTableReturnsPaginatedData() {
        Response response = given().spec(requestSpecification)
                .get("/web/fwm-dashboard/activity-table?date=" + TODAY + "&locationId=null&designationId=null&page=1&pageSize=20");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
    }

    @Test(groups = {"api", "regression"}, description = "Filtered Users by Category, called exactly as the Postman collection defines it, currently errors server-side")
    @Story("Filtered Users by Category")
    @Description("Verified live: HTTP 500 'Internal Server Error' - reproduced with both today's date and the " +
            "Postman collection's own pinned example date (2026-09-16), ruling out a date-related cause. Every " +
            "other Dashboard endpoint (Status Summary, User Attendance, Total Activities, User Login Times, " +
            "Weekly Activity Trend, Activity Table) was individually verified live and returns 200 - this is " +
            "specific to this one endpoint, a real backend bug rather than something wrong with this request. " +
            "Documented as observed behavior per Step 8/21 rather than assuming success.")
    public void filteredUsersByCategoryAsDefinedInPostmanErrors() {
        Response response = given().spec(requestSpecification)
                .get("/web/fwm-dashboard/filtered-users-by-category?date=" + TODAY + "&locationId=null&designationId=null&category=PRESENT");

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), false);
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Status Summary without authentication fails")
    @Story("Status Summary")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void statusSummaryWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification)
                .get("/web/fwm-dashboard/status-summary?date=" + TODAY + "&locationId=null&designationId=null");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
