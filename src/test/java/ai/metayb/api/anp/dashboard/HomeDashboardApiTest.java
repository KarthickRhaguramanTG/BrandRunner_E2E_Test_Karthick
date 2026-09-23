package ai.metayb.api.anp.dashboard;

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
 * "02. ANP / Home Dashboard / Get Counter" (GET /web/dashboard/counter) - the one GET
 * request in the entire ANP Dashboard cluster; every other dashboard request in this
 * folder is POST and blocked (see RequesterDashboardWriteOperationsApiTest and siblings).
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Home Dashboard")
public class HomeDashboardApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Counter returns tenant-wide summary counts")
    @Story("Get Counter")
    @Description("Verified live: HTTP 200, 'Dashboard Data Fetched...', data with totalUsers/totalCompanies/totalProjects/totalTasks/totalMessages.")
    public void getCounterReturnsSummaryCounts() {
        Response response = given().spec(requestSpecification).get("/web/dashboard/counter");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertNotNull(response.jsonPath().get("data.totalUsers"), "Response should include data.totalUsers");
        Assert.assertNotNull(response.jsonPath().get("data.totalTasks"), "Response should include data.totalTasks");
    }

    @Test(groups = { "regression", "negative"}, description = "Get Counter without authentication fails")
    @Story("Get Counter")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getCounterWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/dashboard/counter");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
