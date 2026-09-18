package ai.metayb.api.controlsettings.users;

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
 * "01. Control Settings / Users" - bulk-operation READ requests: Download User Bulk
 * Template, Get Bulk Jobs, Get Bulk Jobs Result. (Upload Users Bulk File is the write
 * counterpart - see UserWriteOperationsApiTest, blocked in this environment.)
 *
 * jobId=1 is real, pre-existing data in this QA tenant (a completed bulk-upload job),
 * not created by this test - discovered via GET, not fabricated.
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Users")
public class UserBulkReadApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "Download User Bulk Template returns an xlsx file")
    @Story("Download User Bulk Template")
    @Description("Verified live: HTTP 200, Content-Type application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, non-empty body.")
    public void downloadUserBulkTemplateReturnsXlsx() {
        Response response = given().spec(requestSpecification).get("/web/user/bulk/template");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(response.contentType().contains("spreadsheetml"), "Expected an xlsx content type, got: " + response.contentType());
        Assert.assertTrue(response.getBody().asByteArray().length > 0, "Template file should not be empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Bulk Jobs returns the status of an existing bulk job")
    @Story("Get Bulk Jobs")
    @Description("Verified live: HTTP 200 for jobId=1 (real pre-existing QA data), status/fileName/totalRows present.")
    public void getBulkJobsReturnsJobStatus() {
        Response response = given().spec(requestSpecification).get("/web/user/bulk/jobs/1");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertNotNull(response.jsonPath().getString("data.status"));
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Bulk Jobs Result returns the result file for a completed job")
    @Story("Get Bulk Jobs Result")
    @Description("Verified live: HTTP 200, Content-Type application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, for jobId=1.")
    public void getBulkJobsResultReturnsResultFile() {
        Response response = given().spec(requestSpecification).get("/web/user/bulk/jobs/1/result");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(response.contentType().contains("spreadsheetml"), "Expected an xlsx content type, got: " + response.contentType());
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Download User Bulk Template without authentication fails")
    @Story("Download User Bulk Template")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void downloadUserBulkTemplateWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/user/bulk/template");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
